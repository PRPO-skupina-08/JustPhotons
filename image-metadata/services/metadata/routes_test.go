package metadata

import (
	"bytes"
	"encoding/json"
	"image-metadata/config"
	"image-metadata/types"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/go-chi/chi/v5"
	"gorm.io/gorm"
)

type mockMetadataStore struct{}

func (m *mockMetadataStore) GetAllMetadata(limit int, offset int, order *string, imgId uint, rating uint, albumId uint) (img []*types.Metadata, result *gorm.DB) {
	result = &gorm.DB{}
	return
}

func (m *mockMetadataStore) GetMetadataById(id uint) (img types.Metadata, result *gorm.DB) {
	result = &gorm.DB{}
	return
}

func (m *mockMetadataStore) InsertMetadata(md *types.Metadata) (*types.Metadata, *gorm.DB) {
	result := &gorm.DB{}
	return md, result
}

func (m *mockMetadataStore) DeleteMetadata(id uint) (result *gorm.DB) {
	result = &gorm.DB{}
	return
}

func (s *mockMetadataStore) DeleteSpecificMetadata(whereClause *[]string) (result *gorm.DB) {
	result = &gorm.DB{}
	return
}

func (h *Handler) abstracted_POST(payload types.InsertMetadataPayload, expected int) func(t *testing.T) {
	return func(t *testing.T) {
		marshalled, _ := json.Marshal(payload)
		endpoint := config.APIVersion + "/images"

		req, err := http.NewRequest(http.MethodPost, endpoint, bytes.NewBuffer(marshalled))
		if err != nil {
			t.Fatal(err)
		}

		rr := httptest.NewRecorder()
		router := chi.NewRouter()

		router.HandleFunc(endpoint, h.handlePostMetadata)
		router.ServeHTTP(rr, req)

		if rr.Code != expected {
			t.Errorf("expected status code %d, got %d", expected, rr.Code)
		}
	}
}

func TestImageServiceHandlers(t *testing.T) {
	metadataStore := &mockMetadataStore{}
	handler := NewHandler(metadataStore)

	t.Run("Should fail due to empty payload", handler.abstracted_POST(
		types.InsertMetadataPayload{},
		http.StatusBadRequest,
	))

	t.Run("Should fail due to bad image_id", handler.abstracted_POST(
		types.InsertMetadataPayload{
			ImageId: 0,
			Rating:  4,
            AlbumId: 1,
		},
		http.StatusBadRequest,
	))

	t.Run("Should fail due to bad album_id", handler.abstracted_POST(
		types.InsertMetadataPayload{
			ImageId: 1,
			Rating:  4,
            AlbumId: 0,
		},
		http.StatusBadRequest,
	))

	t.Run("Should fail due to too high rating", handler.abstracted_POST(
		types.InsertMetadataPayload{
			ImageId: 1,
			Rating:  8,
            AlbumId: 1,
		},
		http.StatusBadRequest,
	))

	t.Run("Should fail due to too low rating", handler.abstracted_POST(
		types.InsertMetadataPayload{
			ImageId: 1,
			Rating:  0,
            AlbumId: 1,
		},
		http.StatusBadRequest,
	))

	t.Run("Should correctly add new data entry", handler.abstracted_POST(
		types.InsertMetadataPayload{
			ImageId: 1,
			Rating:  5,
            AlbumId: 4,
		},
		http.StatusCreated,
	))

	t.Run("Should correctly add new data entry", handler.abstracted_POST(
		types.InsertMetadataPayload{
			ImageId: 1,
			Rating:  1,
            AlbumId: 5,
		},
		http.StatusCreated,
	))

	t.Run("Should correctly add new data entry", handler.abstracted_POST(
		types.InsertMetadataPayload{
			ImageId: 1,
			Rating:  3,
            AlbumId: 6,
		},
		http.StatusCreated,
	))
}
