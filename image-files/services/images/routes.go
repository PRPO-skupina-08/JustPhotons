package images

import (
	"fmt"
	"image-service/types"
	"image-service/utils"
	"net/http"
	"strconv"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
)

type Handler struct {
	store types.ImageStore
}

// Handler == Controller
func NewHandler(store types.ImageStore) *Handler {
	return &Handler{
		store: store,
	}
}

// Router function
func (h *Handler) CreateRoutes(parentRouter *chi.Mux) {
	subrouter := chi.NewRouter()
	parentRouter.Mount("/images", subrouter)

	subrouter.Use(middleware.Logger)

	subrouter.Get("/", h.handleGetAllImages)
	subrouter.Get("/{id}", h.handleGetImage)
	subrouter.Post("/", h.handlePostImage)
}

func (h *Handler) handleGetAllImages(w http.ResponseWriter, r *http.Request) {
	var (
		limit  uint64 = 10
		offset uint64 = 0
		err    error
	)

	if limit_s := r.URL.Query().Get("limit"); limit_s != "" {
		limit, err = strconv.ParseUint(limit_s, 10, 64)
		if err != nil {
			utils.WriteError(w, http.StatusBadRequest, err)
		}
	}

	if offset_s := r.URL.Query().Get("offset"); offset_s != "" {
		offset, err = strconv.ParseUint(offset_s, 10, 64)
		if err != nil {
			utils.WriteError(w, http.StatusBadRequest, err)
		}
	}

	// check if image exists
	img, result := h.store.GetAllImages(int(limit), int(offset))
	if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Image with ID %v doesn't exist", id))
		return
	}

	utils.WriteJSON(w, http.StatusOK, img)
}

func (h *Handler) handleGetImage(w http.ResponseWriter, r *http.Request) {
	paramId := chi.URLParam(r, "id")
	id, err := strconv.ParseUint(paramId, 10, 64)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
	}

	// retrieve image / check if image exists
	img, result := h.store.GetImageById(uint(id))
	if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Image with ID %v doesn't exist", id))
		return
	}

	utils.WriteJSON(w, http.StatusOK, img)
}

func (h *Handler) handlePostImage(w http.ResponseWriter, r *http.Request) {
	// get JSON payload
	var payload types.InsertImagePayload
	if err := utils.ParseJSON(r, &payload); err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}

	// create new image
	result := h.store.InsertImage(&types.Image{
		UserID: payload.UserID,
		Data:   payload.Data,
	})
	if result.Error != nil {
		utils.WriteError(w, http.StatusInternalServerError, result.Error)
		return
	}

	utils.WriteJSON(w, http.StatusCreated, nil)
}
