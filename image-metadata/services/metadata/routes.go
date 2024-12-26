package metadata

import (
	"image-metadata/config"
	"image-metadata/types"
	"net/http"

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
	subrouter.Post("/", h.handlePostMetadata)
	subrouter.Delete("/{id}", h.handleDeleteMetadata)
}

func (h *Handler) handleGetMetadata(w http.ResponseWriter, r *http.Request) {
	// TODO
}

func (h *Handler) handlePostMetadata(w http.ResponseWriter, r *http.Request) {
	// TODO
}

func (h *Handler) handleDeleteMetadata(w http.ResponseWriter, r *http.Request) {
	// TODO
}
