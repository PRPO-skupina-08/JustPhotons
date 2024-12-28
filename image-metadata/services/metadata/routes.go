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
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("No images found! error: %v", result.Error))
		return
	}

	utils.WriteJSON(w, http.StatusOK, md)
}

func (h *Handler) handlePostMetadata(w http.ResponseWriter, r *http.Request) {
	// TODO
}

func (h *Handler) handleDeleteMetadata(w http.ResponseWriter, r *http.Request) {
	// TODO
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
