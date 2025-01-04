package images

import (
	"bytes"
	"encoding/base64"
	"encoding/json"
	"image-service/config"
	"image-service/types"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/go-chi/chi/v5"
	"gorm.io/gorm"
)

type mockImageStore struct{}

func (m *mockImageStore) GetAllImages(limit int, offset int) (img []*types.Image, result *gorm.DB) {
	result = &gorm.DB{}
	return
}

func (m *mockImageStore) GetImageById(id uint) (img types.Image, result *gorm.DB) {
	result = &gorm.DB{}
	return
}

func (m *mockImageStore) InsertImage(img *types.Image) (*types.Image, *gorm.DB) {
    result := &gorm.DB{}
	return nil, result
}

func (m *mockImageStore) DeleteImage(id uint) (result *gorm.DB) {
    result = &gorm.DB{}
	return
}

func (h *Handler) abstracted_POST(payload types.InsertImagePayload, expected int) func(t *testing.T) {
	return func(t *testing.T) {
		marshalled, _ := json.Marshal(payload)
		endpoint := config.APIVersion + "/images"

		req, err := http.NewRequest(http.MethodPost, endpoint, bytes.NewBuffer(marshalled))
		if err != nil {
			t.Fatal(err)
		}

		rr := httptest.NewRecorder()
		router := chi.NewRouter()

		router.HandleFunc(endpoint, h.handlePostImage)
		router.ServeHTTP(rr, req)

		if rr.Code != expected {
			t.Errorf("expected status code %d, got %d", expected, rr.Code)
		}
	}
}

func TestImageServiceHandlers(t *testing.T) {
	imageStore := &mockImageStore{}
	handler := NewHandler(imageStore)

	t.Run("Should fail due to empty data", handler.abstracted_POST(
		types.InsertImagePayload{
			Filename: "name.jpg",
			Data:     base64.StdEncoding.EncodeToString([]byte{}),
		},
		http.StatusBadRequest,
	))

	t.Run("Should fail due to empty filename", handler.abstracted_POST(
		types.InsertImagePayload{
			Filename: "",
			Data:     base64.StdEncoding.EncodeToString([]byte{0xCA, 0xFE, 0xBA, 0xBE}), // ultimate Java sarcasm
		},
		http.StatusBadRequest,
	))

	t.Run("Should fail due to not jpg", handler.abstracted_POST(
		types.InsertImagePayload{
			Filename: "name.random",
			Data:     base64.StdEncoding.EncodeToString([]byte{0xCA, 0xFE, 0xBA, 0xBE}), // ultimate Java sarcasm
		},
		http.StatusBadRequest,
	))

	t.Run("Should correctly add new data entry", handler.abstracted_POST(
		types.InsertImagePayload{
			Filename: "name.jpg",
			Data:     base64.StdEncoding.EncodeToString([]byte{0xCA, 0xFE, 0xBA, 0xBE}), // ultimate Java sarcasm
		},
		http.StatusCreated,
	))
}
