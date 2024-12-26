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

func (s *Store) InsertMetadata(img *types.Metadata) (result *gorm.DB) {
	result = s.db.Create(&img)
	return
}

func (s *Store) DeleteMetadata(id uint) (result *gorm.DB) {
	result = s.db.Delete(&types.Metadata{}, id)
	return
}
