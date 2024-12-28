package metadata

import (
	"fmt"
	"image-metadata/types"
	"log"

	"gorm.io/gorm"
)

type Store struct {
	db *gorm.DB
}

func NewStore(db *gorm.DB) *Store {
	return &Store{db: db}
}

func (s *Store) GetMetadataById(id uint) (img types.Metadata, result *gorm.DB) {
	result = s.db.First(img, id)
	return
}

func (s *Store) GetAllMetadata(limit int, offset int, order *string, imgID uint, rating uint) (md []*types.Metadata, result *gorm.DB) {
	result = s.db.Limit(limit).Offset(offset)
	if order != nil {
		if len(*order) > 0 {
			result = result.Order(order)
		}
	}
	if imgID > 0 {
		result = result.Where(&types.Metadata{ImageId: imgID, Rating: rating})
	}
	if rating > 0 {
		result = result.Where(&types.Metadata{ImageId: imgID, Rating: rating})
	}
	result.Find(&md)
	return
}

func (s *Store) InsertMetadata(md *types.Metadata) (*types.Metadata, *gorm.DB) {
	result := s.db.Create(&md)
	return md, result
}

func (s *Store) DeleteMetadata(id uint) (result *gorm.DB) {
	result = s.db.Unscoped().Delete(&types.Metadata{
		Model: gorm.Model{
			ID: id,
		},
	})
	log.Printf("id: \"%d\", gorm.DB: error: \"%s\", rows affected: \"%d\"\n", id, result.Error, result.RowsAffected)
	return
}

func (s *Store) DeleteSpecificMetadata(whereClause *[]string) (result *gorm.DB) {
	log.Printf("Starting specific delete...")
	if whereClause == nil || len(*whereClause) == 0 {
		result = &gorm.DB{
			Error: fmt.Errorf("Result too broad! Please constrain at least one of either imageID or rating"),
		}
		return
	}

	result = s.db
	for _, i := range *whereClause {
		log.Printf("Adding the where clause: \"%s\"\n", i)
		result = result.Where(i)
	}

	log.Printf("All where clauses added.\n")

	result = result.Unscoped().Delete(&types.Metadata{})
	log.Printf("gorm.DB: error: \"%s\", rows affected: \"%d\"\n", result.Error, result.RowsAffected)
	return
}
