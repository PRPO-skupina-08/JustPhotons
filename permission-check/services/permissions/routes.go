package permissions

import (
	"fmt"
	"net/http"
	"permission-check/config"
	"permission-check/types"
	"permission-check/utils"
	"regexp"
	"slices"
	"strconv"
	"strings"

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

	subrouter.Get("/{id}", h.handleGetPermissions)
	subrouter.Get("/", h.handleGetSpecificPermission)
	subrouter.Post("/", h.handlePostPermission)
	subrouter.Delete("/{id}", h.handleDeletePermission)
}

func (h *Handler) handleGetPermissions(w http.ResponseWriter, r *http.Request) {
	paramId := chi.URLParam(r, "id")
	id, err := strconv.ParseUint(paramId, 10, 64)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
	}

	// retrieve image / check if image exists
	img, result := h.store.GetPermissionById(uint(id))
	if result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Image with ID %v doesn't exist. Error: %v", id, result.Error))
		return
	}

	utils.WriteJSON(w, http.StatusOK, img)
}

func (h *Handler) handleGetSpecificPermission(w http.ResponseWriter, r *http.Request) {
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

	orgId, err := getURLQuery(r, "org_id", parseUintWrapper(), 0)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}

	userId, err := getURLQuery(r, "user_id", parseUintWrapper(), 0)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}

	// check if image exists
	md, result := h.store.GetSpecificPermission(int(limit), int(offset), uint(orgId), uint(userId))
	if result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("No images found! error: %v", result.Error))
		return
	}

	utils.WriteJSON(w, http.StatusOK, md)
}

func (h *Handler) handlePostPermission(w http.ResponseWriter, r *http.Request) {
	// get JSON payload
	var payload types.InsertPermissionPayload
	if err := utils.ParseJSON(r, &payload); err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}

	if payload.OrgId <= 0 {
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("Organization ID must be > 0!"))
		return
	}

	if payload.UserId <= 0 {
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("User ID must be > 0!"))
		return
	}

	existingPermission, err := h.store.GetSpecificPermission(1, 0, payload.OrgId, 0)
	if err != nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Failed to fetch permission: %v", err))
		return
	}

	if existingPermission != nil && len(existingPermission) > 0 {
		// Update UserIds if the entry already exists
		if slices.Contains(existingPermission[0].UserIds, payload.UserId) {
			existingPermission[0].UserIds = append(existingPermission[0].UserIds, payload.UserId)
			if err := h.store.UpdatePermission(existingPermission[0].ID, existingPermission[0]); err != nil {
				utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Failed to update permission: %v", err))
				return
			}
		}
		utils.WriteJSON(w, http.StatusOK, existingPermission)
		return
	}

	// create new permission entry
	md, result := h.store.InsertPermission(&types.Permission{
		OrgId:   payload.OrgId,
		UserIds: []uint{payload.UserId},
	})
	if result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB post result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusInternalServerError, result.Error)
		return
	}

	utils.WriteJSON(w, http.StatusCreated, md)
}

func (h *Handler) handleDeletePermission(w http.ResponseWriter, r *http.Request) {
	paramId := chi.URLParam(r, "id")
	id, err := strconv.ParseUint(paramId, 10, 64)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
	}

	if result := h.store.DeletePermission(uint(id)); result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Metadata with ID %v doesn't exist. Error: %v", id, result.Error))
		return
	} else if result.RowsAffected == 0 {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Metadata with ID %v doesn't exist (rows affected == 0).", id))
		return
	}

	utils.WriteJSON(w, http.StatusNoContent, nil)
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
