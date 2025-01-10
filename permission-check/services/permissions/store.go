package permissions

import (
	"fmt"
	"log"
	"permission-check/types"
	"strconv"

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

func (s *Store) GetSpecificPermission(limit int, offset int, orgId uint, userId uint) (p []*types.Permission, result *gorm.DB) {
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

func (s *Store) DeleteSpecificPermission(userId uint64, orgId uint64) (result *gorm.DB) {
    var whereClauses []string = make([]string, 0)

	if userId != 0 {
		whereClauses = append(whereClauses, "user_id = "+strconv.FormatUint(userId, 10))
	}
	if orgId != 0 {
		whereClauses = append(whereClauses, "org_id = "+strconv.FormatUint(orgId, 10))
	}

	log.Printf("Starting specific delete...")
	if whereClauses == nil || len(whereClauses) == 0 {
		result = &gorm.DB{
			Error: fmt.Errorf("Result too broad! Please constrain at least one of either user_id or org_id"),
		}
		return
	}

	result = s.db
	for _, i := range whereClauses {
		log.Printf("Adding the where clause: \"%s\"\n", i)
		result = result.Where(i)
	}

	log.Printf("All where clauses added.\n")

	result = result.Unscoped().Delete(&types.Permission{})
	log.Printf("gorm.DB: error: \"%s\", rows affected: \"%d\"\n", result.Error, result.RowsAffected)
	return
}
