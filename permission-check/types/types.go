package types

import "gorm.io/gorm"

type MetadataStore interface {
	GetPermissionById(uint) (Permission, *gorm.DB)
	GetSpecificPermission(limit int, offset int, orgId uint) ([]*Permission, *gorm.DB)
    GetPermissionFromUser(limit int, offset int) ([]*User, *gorm.DB)
	InsertPermission(*Permission) (*Permission, *gorm.DB)
	DeletePermission(uint) *gorm.DB
	UpdatePermission(id uint, p *Permission) *gorm.DB
}

type Permission struct {
	gorm.Model
	OrgId   uint   `gorm:"not null;check:org_id <> '';check:org_id > 0;unique"`
	UserIds []User `gorm:"foreignKey:UserRefer"`
}

type User struct {
	gorm.Model
    UserRefer uint
}

type InsertPermissionPayload struct {
	OrgId  uint `json:"org_id"`
	UserId uint `json:"user_id"`
}
