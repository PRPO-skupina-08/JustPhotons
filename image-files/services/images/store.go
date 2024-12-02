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

func (s *Store) GetImageById(id int) (img types.Image, result *gorm.DB) {
    result = s.db.First(img, id)
    return
}

func (s *Store) InsertImage(img *types.Image) (result *gorm.DB) {
    result = s.db.Create(&img)
    return
}
