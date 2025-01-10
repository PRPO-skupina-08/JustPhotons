package types

import "gorm.io/gorm"

type MetadataStore interface {
	GetPermissionById(uint) (Permission, *gorm.DB)
	GetSpecificPermission(limit int, offset int, orgId uint, userId uint) ([]*Permission, *gorm.DB)
	InsertPermission(*Permission) (*Permission, *gorm.DB)
	DeletePermission(uint) *gorm.DB
	DeleteSpecificPermission(userId uint64, orgId uint64) *gorm.DB
}

type Permission struct {
	gorm.Model
	OrgId  uint `gorm:"not null;check:org_id <> '';check:org_id > 0"`
	UserId uint `gorm:"not null;check:user_id <> '';check:user_id > 0"`
}

type InsertPermissionPayload struct {
	OrgId  uint `json:"org_id"`
	UserId uint `json:"user_id"`
}
