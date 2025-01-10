package permissions

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"permission-check/config"
	"permission-check/types"
	"testing"

	"github.com/go-chi/chi/v5"
	"gorm.io/gorm"
)

type mockMetadataStore struct{}

func (m *mockMetadataStore) GetPermissionById(id uint) (perm types.Permission, result *gorm.DB) {
	result = &gorm.DB{}
	return
}

func (m *mockMetadataStore) GetSpecificPermission(limit int, offset int, orgId uint, userId uint) (perms []*types.Permission, result *gorm.DB) {
	result = &gorm.DB{}
	return
}

func (m *mockMetadataStore) InsertPermission(perm *types.Permission) (*types.Permission, *gorm.DB) {
	result := &gorm.DB{}
	return perm, result
}

func (m *mockMetadataStore) DeletePermission(uint) (result *gorm.DB) {
	result = &gorm.DB{}
	return
}

func (m *mockMetadataStore) DeleteSpecificPermission(userId uint64, orgId uint64) (result *gorm.DB) {
	result = &gorm.DB{}
	return
}

func (h *Handler) abstracted_POST(payload types.InsertPermissionPayload, expected int) func(t *testing.T) {
	return func(t *testing.T) {
		marshalled, _ := json.Marshal(payload)
		endpoint := config.APIVersion + config.Subroute

		req, err := http.NewRequest(http.MethodPost, endpoint, bytes.NewBuffer(marshalled))
		if err != nil {
			t.Fatal(err)
		}

		rr := httptest.NewRecorder()
		router := chi.NewRouter()

		router.HandleFunc(endpoint, h.handlePostPermission)
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
		types.InsertPermissionPayload{},
		http.StatusBadRequest,
	))

	t.Run("Should fail due to bad org_id", handler.abstracted_POST(
		types.InsertPermissionPayload{
            OrgId: 0,
            UserId: 1,
		},
		http.StatusBadRequest,
	))

	t.Run("Should fail due to bad user_id", handler.abstracted_POST(
		types.InsertPermissionPayload{
            OrgId: 1,
            UserId: 0,
		},
		http.StatusBadRequest,
	))

	t.Run("Should correctly add new data entry", handler.abstracted_POST(
		types.InsertPermissionPayload{
            OrgId: 1,
            UserId: 1,
		},
		http.StatusCreated,
	))

	t.Run("Should correctly add new data entry", handler.abstracted_POST(
		types.InsertPermissionPayload{
            OrgId: 2,
            UserId: 1,
		},
		http.StatusCreated,
	))

	t.Run("Should correctly add new data entry", handler.abstracted_POST(
		types.InsertPermissionPayload{
            OrgId: 1,
            UserId: 2,
		},
		http.StatusCreated,
	))
}
