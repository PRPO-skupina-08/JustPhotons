package images

import (
	"image-service/types"

	"gorm.io/gorm"
)

type Store struct {
	db *gorm.DB
}

func NewStore(db *gorm.DB) *Store {
	return &Store{db: db}
}

func (s *Store) GetAllImages(limit int, offset int) (img []*types.Image, result *gorm.DB) {
	result = s.db.Limit(limit).Offset(offset).Find(img)
	return
}

func (s *Store) GetImageById(id uint) (img types.Image, result *gorm.DB) {
	result = s.db.First(img, id)
	return
}

func (s *Store) InsertImage(img *types.Image) (result *gorm.DB) {
	result = s.db.Create(&img)
	return
}

func (s *Store) DeleteImage(id uint) (result *gorm.DB) {
	result = s.db.Delete(&types.Image{}, id)
	return
}
