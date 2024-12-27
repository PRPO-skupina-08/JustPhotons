package metadata

import (
	"fmt"
	"image-metadata/config"
	"image-metadata/types"
	"image-metadata/utils"
	"net/http"
	"strconv"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
)

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
	// TODO
}

func (h *Handler) handlePostMetadata(w http.ResponseWriter, r *http.Request) {
	// TODO
}

func (h *Handler) handleDeleteMetadata(w http.ResponseWriter, r *http.Request) {
	// TODO
}
