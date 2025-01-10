package metadata

import (
	"fmt"
	"image-metadata/config"
	"image-metadata/types"
	"image-metadata/utils"
	"net/http"
	"regexp"
	"strconv"
	"strings"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	_ "image-metadata/cmd/docs"
)

type SortToken struct {
	Field string
	Order string
}

type Handler struct {
	store types.MetadataStore
}

// Handler == Controller
func NewHandler(store types.MetadataStore) *Handler {
	return &Handler{
		store: store,
	}
}

// Router function
func (h *Handler) CreateRoutes(parentRouter *chi.Mux) {
	subrouter := chi.NewRouter()
	parentRouter.Mount(config.Subroute, subrouter)

	subrouter.Use(middleware.Logger)

	subrouter.Get("/{id}", h.handleGetMetadata)
	subrouter.Get("/", h.handleGetAllMetadata)
	subrouter.Post("/", h.handlePostMetadata)
	subrouter.Delete("/{id}", h.handleDeleteMetadata)
	subrouter.Delete("/", h.handleDeleteSpecificMetadata)
}

// GetMetadata godoc
//
//	@Summary		Get single Metadata entry
//	@Description	Retrieves a specific metadata entry based on its ID.
//	@Tags			metadata
//	@Produce		json
//	@Param			id	path		uint			true	"Metadata entry ID"	example(42)
//	@Success		200	{object}	types.Metadata	"Matching entry"
//	@Failure		400	{object}	error			"Incorrect input"
//	@Failure		404	{object}	error			"No results"
//	@Failure		500	{object}	error			"Internal server error"
//	@Router			/metadata/{id} [get]
func (h *Handler) handleGetMetadata(w http.ResponseWriter, r *http.Request) {
	paramId := chi.URLParam(r, "id")
	id, err := strconv.ParseUint(paramId, 10, 64)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
	}

	// retrieve image / check if image exists
	img, result := h.store.GetMetadataById(uint(id))
	if result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Image with ID %v doesn't exist. Error: %v", id, result.Error))
		return
	}

	utils.WriteJSON(w, http.StatusOK, img)
}

// GetAllMetadata godoc
//
//	@Summary		Get many Metadata entries
//	@Description	Retrieves many metadata entries based on its query parameters.
//	@Tags			metadata
//	@Produce		json
//	@Param			limit		query		uint			false	"Maximum amount of returned entries (>0). To be used with `offset` in order to achieve pagination."							example(20)
//	@Param			offset		query		uint			false	"Amount of entries left out at the start. To be used with `limit` in order to achieve pagination."							example(10)
//	@Param			sort		query		string			false	"SQL sorting in with pattern `<field>:<order>[,]...`, first pattern does primary sort, second pattern secondary sort etc."	example(rating:asc,image_id:desc)
//	@Param			image_id	query		uint			false	"The ID of the image to which the metadata entry belongs."																	example(42)
//	@Param			rating		query		uint			false	"Image rating, between 1 and 5 (inclusive)"																					example(4)	minimum(0)	maximum(5)
//	@Param			album_id	query		uint			false	"The ID of the album to which the image belongs"																			example(42)
//	@Success		200			{object}	types.Metadata	"Matching entry"
//	@Failure		400			{object}	error			"Incorrect input"
//	@Failure		404			{object}	error			"No results"
//	@Failure		500			{object}	error			"Internal server error"
//	@Router			/metadata [get]
func (h *Handler) handleGetAllMetadata(w http.ResponseWriter, r *http.Request) {
	limit, err := getURLQuery(r, "limit", parseUintWrapper(), 20)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}
	// TODO: do this with the validate library.
	if limit <= 0 {
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("Limit cannot be 0!"))
		return
	}

	offset, err := getURLQuery(r, "offset", parseUintWrapper(), 0)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}

	orderBy, err := getURLQuery(r, "sort", parseSortParams, nil)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}

	imageId, err := getURLQuery(r, "image_id", parseUintWrapper(), 0)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}

	rating, err := getURLQuery(r, "rating", parseUintWrapper(), 0)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}

	albumId, err := getURLQuery(r, "album_id", parseUintWrapper(), 0)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}

	// check if image exists
	md, result := h.store.GetAllMetadata(int(limit), int(offset), orderBy, uint(imageId), uint(rating), uint(albumId))
	if result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("No images found! error: %v", result.Error))
		return
	}

	utils.WriteJSON(w, http.StatusOK, md)
}

// PostMetadata godoc
//
//	@Summary		Insert/create a new Metadata entry
//	@Description	Creates a new metadata entry in the database
//	@Tags			metadata
//	@Accept			json
//	@Produce		json
//	@Param			metadata	body		types.InsertMetadataPayload	true	"Insert metadata payload"	example({ "image_id": 42, "rating": 4, "album_id": 2 })
//	@Success		201			{object}	types.Metadata				"Successfully created new entry"
//	@Failure		400			{object}	error						"Incorrect input (missing fields, incorrect data etc.)"
//	@Failure		500			{object}	error						"Internal server error, but can be caused by database rejecting wrong data (would be a developer's mistake)."
//	@Router			/metadata [post]
func (h *Handler) handlePostMetadata(w http.ResponseWriter, r *http.Request) {
	// get JSON payload
	var payload types.InsertMetadataPayload
	if err := utils.ParseJSON(r, &payload); err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}

	if payload.Rating < 1 || 5 < payload.Rating {
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("Incorrect rating field! Must be between 1 and 5"))
		return
	}

	if payload.ImageId <= 0 {
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("image_id is 0! Perhaps it's missing?"))
		return
	}

	if payload.AlbumId <= 0 {
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("album_id is 0! Perhaps it's missing?"))
		return
	}

	// create new metadata
	md, result := h.store.InsertMetadata(&types.Metadata{
		ImageId: payload.ImageId,
		AlbumId: payload.AlbumId,
		Rating:  payload.Rating,
	})
	if result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB post result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusInternalServerError, result.Error)
		return
	}

	utils.WriteJSON(w, http.StatusCreated, md)
}

// DeleteMetadata godoc
//
//	@Summary		Delete a single Metadata entry
//	@Description	Deletes a specific metadata entry based on its ID.
//	@Tags			metadata
//	@Produce		json
//	@Param			id	path		uint	true	"Metadata entry ID"	example(42)
//	@Success		204	{object}	nil		"Sucessfully deleted"
//	@Failure		400	{object}	error	"Incorrect input"
//	@Failure		404	{object}	error	"Nothing to delete"
//	@Failure		500	{object}	error	"Internal server error"
//	@Router			/metadata/{id} [delete]
func (h *Handler) handleDeleteMetadata(w http.ResponseWriter, r *http.Request) {
	paramId := chi.URLParam(r, "id")
	id, err := strconv.ParseUint(paramId, 10, 64)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
	}

	if result := h.store.DeleteMetadata(uint(id)); result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Metadata with ID %v doesn't exist. Error: %v", id, result.Error))
		return
	} else if result.RowsAffected == 0 {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Metadata with ID %v doesn't exist (rows affected == 0).", id))
		return
	}

	utils.WriteJSON(w, http.StatusNoContent, nil)
}

// DeleteSpecificMetadata godoc
//
//	@Summary		Delete many Metadata entries
//	@Description	Deletes many metadata entries based on its query parameters. **At least one paramater must be present!**
//	@Tags			metadata
//	@Produce		json
//	@Param			image_id	query		uint	false	"Image ID (not the metadata entry's ID)"	example(42)
//	@Param			rating		query		uint	false	"Image rating, between 1 and 5 (inclusive)"	example(4)
//	@Param			album_id	query		uint	false	"Image ID"									example(4)
//	@Success		204			{object}	nil		"Sucessfully deleted"
//	@Failure		400			{object}	error	"Incorrect input"
//	@Failure		400			{object}	error	"Neither `image_id` nor `rating` specified, but at least one is needed"
//	@Failure		404			{object}	error	"No results"
//	@Failure		500			{object}	error	"Internal server error"
//	@Router			/metadata [delete]
func (h *Handler) handleDeleteSpecificMetadata(w http.ResponseWriter, r *http.Request) {
	var whereClauses []string = make([]string, 0)

	imageId, err := getURLQuery(r, "image_id", parseUintWrapper(), 0)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}
	if imageId != 0 {
		whereClauses = append(whereClauses, "image_id = "+strconv.FormatUint(imageId, 10))
	}

	rating, err := getURLQuery(r, "rating", parseUintWrapper(), 0)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}
	if rating != 0 {
		whereClauses = append(whereClauses, "rating = "+strconv.FormatUint(rating, 10))
	}

	albumId, err := getURLQuery(r, "album_id", parseUintWrapper(), 0)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}
	if albumId != 0 {
		whereClauses = append(whereClauses, "album_id = "+strconv.FormatUint(albumId, 10))
	}

	if len(whereClauses) <= 0 {
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("Result too broad! Please constrain at least one of either imageID or rating"))
		return
	}

	if result := h.store.DeleteSpecificMetadata(&whereClauses); result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Metadata with specified parameters doesn't exist. Error: %v", result.Error))
		return
	} else if result.RowsAffected == 0 {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Metadata with specified parameters doesn't exist (rows affected == 0)."))
		return
	}

	utils.WriteJSON(w, http.StatusNoContent, nil)
}

func getURLQuery[T any](r *http.Request, parameter string, convertFunc func(string) (T, error), defaultVal T) (converted T, err error) {
	whatStr := r.URL.Query().Get(parameter)
	if whatStr == "" {
		return defaultVal, nil
	}

	// Attempt conversion using the provided function
	if convertFunc != nil {
		converted, err = convertFunc(whatStr)
		if err != nil {
			var zero T
			return zero, err
		}
	}

	return
}

func parseUintWrapper() func(string) (uint64, error) {
	return func(s string) (uint64, error) {
		return strconv.ParseUint(s, 10, 64)
	}
}

func parseSortParams(input string) (*string, error) {
	// Define the regular expression to match a field:order pair
	re := regexp.MustCompile(`^([a-zA-Z]+):(asc|desc)$`)

	// Split the input string by commas
	parts := strings.Split(input, ",")

	result := ""
	first := true

	for _, part := range parts {
		part = strings.TrimSpace(part) // Remove leading/trailing spaces
		match := re.FindStringSubmatch(part)
		if match == nil {
			return nil, fmt.Errorf("invalid sort parameter: %s", part)
		}

		newStr := match[1] + " " + match[2]
		if !first {
			newStr = ", " + newStr
		} else {
			first = false
		}

		result += newStr
	}

	return &result, nil
}
