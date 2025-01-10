package types

import "gorm.io/gorm"

type ImageStore interface {
	GetAllImages(limit int, offset int) (img []*Image, result *gorm.DB)
	GetImageById(id uint) (img Image, result *gorm.DB)
	InsertImage(img *Image) (*Image, *gorm.DB)
	DeleteImage(id uint) (result *gorm.DB)
}

type Image struct {
	gorm.Model
	Filename string `gorm:"not null;check:filename<>''"`
	Data     []byte `gorm:"not null;check:data<>''"`
}

type InsertImagePayload struct {
	Filename string `json:"filename"`
	Data     string `json:"data"`
}
