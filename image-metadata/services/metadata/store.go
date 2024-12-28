package metadata

import (
	"image-metadata/types"

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
    if len(*order) > 0 {
        result = result.Order(order)
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
	result = s.db.Delete(&types.Metadata{}, id)
	return
}
