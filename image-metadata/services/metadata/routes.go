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

func (h *Handler) handleGetAllMetadata(w http.ResponseWriter, r *http.Request) {
	limit, err := getURLQuery(r, "limit", parseUintWrapper(), 20)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
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

	imageID, err := getURLQuery(r, "imageID", parseUintWrapper(), 0)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}

	rating, err := getURLQuery(r, "rating", parseUintWrapper(), 0)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}

	// check if image exists
	md, result := h.store.GetAllMetadata(int(limit), int(offset), orderBy, uint(imageID), uint(rating))
	if result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("No images found! error: %v", result.Error))
		return
	}

	utils.WriteJSON(w, http.StatusOK, md)
}

func (h *Handler) handlePostMetadata(w http.ResponseWriter, r *http.Request) {
	// get JSON payload
	var payload types.InsertMetadataPayload
	if err := utils.ParseJSON(r, &payload); err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}

	if payload.Rating < 0 || 5 < payload.Rating {
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("Incorrect rating field! Must be between 0 and 5"))
		return
	}

	if payload.ImageId == 0 {
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("Empty filename field!"))
		return
	}

	/**
	* This is just a debug function - it creates a test_image.{jpg, png ...} file to
	* check if bytes were written correctly.
	 */
	/*
	   if os.WriteFile("test_image"+strings.ToLower(path.Ext(payload.Filename)), imageData, 0644) != nil {
	       log.Printf("Error writing file '%s': %v\n", payload.Filename, err)
	   }
	*/

	// create new image
	md, result := h.store.InsertMetadata(&types.Metadata{
		ImageId: payload.ImageId,
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

func (h *Handler) handleDeleteSpecificMetadata(w http.ResponseWriter, r *http.Request) {
	var whereClauses []string = make([]string, 0)

    imageID, err := getURLQuery(r, "image_id", parseUintWrapper(), 0)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}
	if imageID != 0 {
		whereClauses = append(whereClauses, "image_id = "+strconv.FormatUint(imageID, 10))
	}

	rating, err := getURLQuery(r, "rating", parseUintWrapper(), 0)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}
	if rating != 0 {
		whereClauses = append(whereClauses, "rating = "+strconv.FormatUint(rating, 10))
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
