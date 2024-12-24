package types

import "gorm.io/gorm"

type ImageStore interface {
	GetAllImages(limit int, offset int) (img []*Image, result *gorm.DB)
	GetImageById(id uint) (img Image, result *gorm.DB)
	InsertImage(img *Image) (result *gorm.DB)
	DeleteImage(id uint) (result *gorm.DB)
}

type Image struct {
	gorm.Model
	Data []byte `gorm:"not null;check:data<>''"`
}

type InsertImagePayload struct {
	Data []byte `json:"data"`
}
