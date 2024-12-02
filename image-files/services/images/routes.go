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

func NewHandler(store types.ImageStore) *Handler {
    return &Handler{
        store: store,
    }
}

func (h *Handler) CreateRoutes(parentRouter *chi.Mux) {
    router := chi.NewRouter()
    parentRouter.Mount("/images", router)

    router.Use(middleware.Logger)

    router.Get("/{id}", h.handleGetImage)
    router.Post("/", h.handlePostImage)
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
        Data: payload.Data,
        UserID: payload.UserID,
    })
	if result.Error != nil {
		utils.WriteError(w, http.StatusInternalServerError, result.Error)
		return
	}

	utils.WriteJSON(w, http.StatusCreated, nil)
}

func (h *Handler) handleGetImage(w http.ResponseWriter, r *http.Request) {
    paramId := chi.URLParam(r, "id")
    id, err := strconv.ParseUint(paramId, 10, 64)
    if err != nil {
        utils.WriteError(w, http.StatusBadRequest, err)
    }

	// check if image exists
	img, result := h.store.GetImageById(int(id))
	if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Image with ID %s doesn't exist", id))
		return
	}

    utils.WriteJSON(w, http.StatusOK, img)
}
