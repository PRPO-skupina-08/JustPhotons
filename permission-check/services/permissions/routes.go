package permissions

import (
	"fmt"
	"log"
	"net/http"
	"permission-check/config"
	"permission-check/types"
	"permission-check/utils"
	"regexp"
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
	subrouter.Delete("/", h.handleDeleteSpecificPermission)
}

func (h *Handler) handleGetPermissions(w http.ResponseWriter, r *http.Request) {
	paramId := chi.URLParam(r, "id")
	id, err := strconv.ParseUint(paramId, 10, 64)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
	}

	// retrieve permission / check if permission exists
	perm, result := h.store.GetPermissionById(uint(id))
	if result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Permission with ID %v doesn't exist. Error: %v", id, result.Error))
		return
	}

	utils.WriteJSON(w, http.StatusOK, perm)
}

func (h *Handler) handleGetSpecificPermission(w http.ResponseWriter, r *http.Request) {
	limit, err := getURLQuery(r, "limit", parseUintWrapper(), 20)
	if err != nil {
		utils.WriteError(w, http.StatusBadRequest, err)
		return
	}
	if limit <= 0 {
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("Limit cannot be 0!"))
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

	p, result := h.store.GetSpecificPermission(int(limit), int(offset), uint(orgId), uint(userId))

	// check if permission exists
	if result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("No permissions found! error: %v", result.Error))
		return
	}

	utils.WriteJSON(w, http.StatusOK, p)
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

	existingPermission, result := h.store.GetSpecificPermission(1, 0, payload.OrgId, payload.UserId)
	if result.Error != nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Failed to fetch permission: %v", result.Error))
		return
	}

    log.Printf("exits: %v", existingPermission)
	if existingPermission != nil && len(existingPermission) > 0 {
        log.Printf("org: %v, usr: %v", existingPermission[0].OrgId, existingPermission[0].UserId)
		if existingPermission[0].OrgId == payload.OrgId && existingPermission[0].UserId == payload.UserId {
			utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("This org_id and user_id combination already exists."))
			return
		}
	}

	// create new permission entry
	perm, result := h.store.InsertPermission(&types.Permission{
		OrgId:  payload.OrgId,
		UserId: payload.UserId,
	})
	if result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB post result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusInternalServerError, result.Error)
		return
	}

	utils.WriteJSON(w, http.StatusCreated, perm)
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

func (h *Handler) handleDeleteSpecificPermission(w http.ResponseWriter, r *http.Request) {
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

	if orgId == 0 && userId == 0 {
		utils.WriteError(w, http.StatusBadRequest, fmt.Errorf("Result too broad! Please constrain at least one of either org_id or user_id!"))
		return
	}

	if result := h.store.DeleteSpecificPermission(userId, orgId); result == nil {
		utils.WriteError(w, http.StatusInternalServerError, fmt.Errorf("Internal server error: DB query result is nil"))
		return
	} else if result.Error != nil {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Metadata with specified parameters doesn't exist. Error: %v", result.Error))
		return
	} else if result.RowsAffected == 0 {
		utils.WriteError(w, http.StatusNotFound, fmt.Errorf("Metadata with specified parameters doesn't exist (rows affected == 0)."))
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
