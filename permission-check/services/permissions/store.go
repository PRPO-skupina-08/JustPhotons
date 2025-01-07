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

func (s *Store) GetPermissionFromUser(limit int, offset int) (users []*types.User, result *gorm.DB) {
	result = s.db.Limit(limit).Offset(offset).Preload("UserIds").Find(&users)
	return
}

func (s *Store) GetSpecificPermission(limit int, offset int, orgId uint) (p []*types.Permission, result *gorm.DB) {
	result = s.db.Limit(limit).Offset(offset)

	if orgId > 0 {
		result = result.Where(&types.Permission{OrgId: orgId})
	}

	result.Find(&p)
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
