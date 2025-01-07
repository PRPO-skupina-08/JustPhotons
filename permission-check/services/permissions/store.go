package permissions

import (
	"log"
	"permission-check/types"

	"gorm.io/gorm"
)

type Store struct {
	db *gorm.DB
}

func NewStore(db *gorm.DB) *Store {
	return &Store{db: db}
}

func (s *Store) GetPermissionById(id uint) (p types.Permission, result *gorm.DB) {
	result = s.db.First(p, id)
	return
}

func (s *Store) GetSpecificPermission(limit int, offset int, orgId uint, userID uint) (p []*types.Permission, result *gorm.DB) {
	result = s.db.Limit(limit).Offset(offset)
	var users []uint

	if orgId > 0 {
		result = result.Where(&types.Permission{OrgId: orgId})
	}

	if userID > 0 {
		result = result.Preload("UserIds").Find(&users)
	} else {
		result.Find(&p)
	}
	return
}

func (s *Store) InsertPermission(p *types.Permission) (*types.Permission, *gorm.DB) {
	result := s.db.Create(&p)
	return p, result
}

func (s *Store) DeletePermission(id uint) (result *gorm.DB) {
	result = s.db.Unscoped().Delete(&types.Permission{
		Model: gorm.Model{
			ID: id,
		},
	})
	log.Printf("id: \"%d\", gorm.DB: error: \"%s\", rows affected: \"%d\"\n", id, result.Error, result.RowsAffected)
	return
}

func (s *Store) UpdatePermission(id uint, p *types.Permission) (result *gorm.DB) {
    p.ID = id
    result = s.db.Save(p)
    return
}
