package images

import (
	"image-service/types"
	"log"

	"gorm.io/gorm"
)

type Store struct {
	db *gorm.DB
}

func NewStore(db *gorm.DB) *Store {
	return &Store{db: db}
}

func (s *Store) GetAllImages(limit int, offset int) (img []*types.Image, result *gorm.DB) {
	result = s.db.Limit(limit).Offset(offset).Find(&img)
	return
}

func (s *Store) GetImageById(id uint) (img types.Image, result *gorm.DB) {
	result = s.db.First(&img, id)
	return
}

func (s *Store) InsertImage(img *types.Image) (*types.Image, *gorm.DB) {
	result := s.db.Create(&img)
	return img, result
}

func (s *Store) DeleteImage(id uint) (result *gorm.DB) {
	result = s.db.Unscoped().Delete(&types.Image{
		Model: gorm.Model{
			ID: id,
		},
	})
	log.Printf("id: \"%d\", gorm.DB: error: \"%s\", rows affected: \"%d\"\n", id, result.Error, result.RowsAffected)
	return
}
