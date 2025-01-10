package images

import (
	"encoding/base64"
	"fmt"
	"image-service/types"
	"image-service/utils"
	"net/http"
	"path"
	"strconv"
	"strings"

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
	subrouter.Delete("/{id}", h.handleDeleteImage)
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
			return
		}
	}

	if offset_s := r.URL.Query().Get("offset"); offset_s != "" {
		offset, err = strconv.ParseUint(offset_s, 10, 64)
		if err != nil {
			utils.WriteError(w, http.StatusBadRequest, err)
			return
		}
	}

	// check if image exists
	img, result := h.store.GetAllImages(int(limit), int(offset))
	if result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("No images found! error: %v", result.Error))
		return
	}

	var returnPayload []types.Image = make([]types.Image, 0)

	for _, i := range img {
		returnPayload = append(returnPayload, *i)
	}

	utils.WriteJSON(w, http.StatusOK, returnPayload)
}

func (h *Handler) handleGetImage(w http.ResponseWriter, r *http.Request) {
	paramId := chi.URLParam(r, "id")
	id, err := strconv.ParseUint(paramId, 10, 64)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
	}

	// retrieve image / check if image exists
	img, result := h.store.GetImageById(uint(id))
	if result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Image with ID %v doesn't exist. Error: %v", id, result.Error))
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

	if len(payload.Data) == 0 {
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("Empty data field!"))
		return
	}

	if len(payload.Filename) == 0 {
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("Empty filename field!"))
		return
	}

	switch strings.ToLower(path.Ext(payload.Filename)) {
	case ".jpg":
	case ".jpeg":
	case ".png":
		break
	default:
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("Uploaded file doesn't end in jpg, jpeg or png!"))
		return
	}

	// Decode data from base64
	imageData, err := base64.StdEncoding.DecodeString(payload.Data)
	if err != nil {
		utils.WriteError(w, http.StatusInternalServerError, err)
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
	img, result := h.store.InsertImage(&types.Image{
		Filename: payload.Filename,
		Data:     imageData,
	})
	if result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB post result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusInternalServerError, result.Error)
		return
	}

	utils.WriteJSON(w, http.StatusCreated, img)
}

func (h *Handler) handleDeleteImage(w http.ResponseWriter, r *http.Request) {
	paramId := chi.URLParam(r, "id")

	id, err := strconv.ParseUint(paramId, 10, 64)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
	}

	// retrieve image / check if image exists

	if result := h.store.DeleteImage(uint(id)); result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Image with ID %v doesn't exist. Error: %v", id, result.Error))
		return
	} else if result.RowsAffected == 0 {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Image with ID %v doesn't exist (rows affected == 0).", id))
		return
	}

	utils.WriteJSON(w, http.StatusNoContent, nil)
}
