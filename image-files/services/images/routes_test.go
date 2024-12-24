package images

import (
	"bytes"
	"encoding/json"
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

func (m *mockImageStore) InsertImage(img *types.Image) (result *gorm.DB) {
	result = &gorm.DB{}
	return
}

func (m *mockImageStore) DeleteImage(id uint) (result *gorm.DB) {
	result = &gorm.DB{}
	return
}

func TestImageServiceHandlers(t *testing.T) {
	imageStore := &mockImageStore{}
	handler := NewHandler(imageStore)

	t.Run("Should fail due to empty data", func(t *testing.T) {
		payload := types.InsertImagePayload{
			Filename: "name.jpg",
			Data:     []byte{},
		}

		marshalled, _ := json.Marshal(payload)

		req, err := http.NewRequest(http.MethodPost, "/api/v1/images", bytes.NewBuffer(marshalled))
		if err != nil {
			t.Fatal(err)
		}

		rr := httptest.NewRecorder()
		router := chi.NewRouter()

		router.HandleFunc("/api/v1/images", handler.handlePostImage)
		router.ServeHTTP(rr, req)

		expected := http.StatusBadRequest
		if rr.Code != expected {
			t.Errorf("expected status code %d, got %d", expected, rr.Code)
		}
	})

	t.Run("Should fail due to empty filename", func(t *testing.T) {
		payload := types.InsertImagePayload{
			Filename: "",
			Data:     []byte{0xCA, 0xFE, 0xBA, 0xBE}, // ultimate Java sarcasm
		}

		marshalled, _ := json.Marshal(payload)

		req, err := http.NewRequest(http.MethodPost, "/api/v1/images", bytes.NewBuffer(marshalled))
		if err != nil {
			t.Fatal(err)
		}

		rr := httptest.NewRecorder()
		router := chi.NewRouter()

		router.HandleFunc("/api/v1/images", handler.handlePostImage)
		router.ServeHTTP(rr, req)

		expected := http.StatusBadRequest
		if rr.Code != expected {
			t.Errorf("expected status code %d, got %d", expected, rr.Code)
		}
	})

	t.Run("Should fail due to not jpg", func(t *testing.T) {
		payload := types.InsertImagePayload{
			Filename: "name.random",
			Data:     []byte{0xCA, 0xFE, 0xBA, 0xBE}, // ultimate Java sarcasm
		}

		marshalled, _ := json.Marshal(payload)

		req, err := http.NewRequest(http.MethodPost, "/api/v1/images", bytes.NewBuffer(marshalled))
		if err != nil {
			t.Fatal(err)
		}

		rr := httptest.NewRecorder()
		router := chi.NewRouter()

		router.HandleFunc("/api/v1/images", handler.handlePostImage)
		router.ServeHTTP(rr, req)

		expected := http.StatusBadRequest
		if rr.Code != expected {
			t.Errorf("expected status code %d, got %d", expected, rr.Code)
		}
	})

	t.Run("Should correctly add new data entry", func(t *testing.T) {
		payload := types.InsertImagePayload{
			Filename: "name.jpg",
			Data:     []byte{0xCA, 0xFE, 0xBA, 0xBE}, // ultimate Java sarcasm
		}

		marshalled, _ := json.Marshal(payload)

		req, err := http.NewRequest(http.MethodPost, "/api/v1/images", bytes.NewBuffer(marshalled))
		if err != nil {
			t.Fatal(err)
		}

		rr := httptest.NewRecorder()
		router := chi.NewRouter()

		router.HandleFunc("/api/v1/images", handler.handlePostImage)
		router.ServeHTTP(rr, req)

		expected := http.StatusCreated
		if rr.Code != expected {
			t.Errorf("expected status code %d, got %d", expected, rr.Code)
		}
	})
}
