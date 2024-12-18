package types

import "gorm.io/gorm"

type ImageStore interface {
	GetAllImages(limit int, offset int) (img Image, result *gorm.DB)
	GetImageById(id uint) (img Image, result *gorm.DB)
	InsertImage(img *Image) (result *gorm.DB)
	DeleteImage(img *Image) (result *gorm.DB)
}

type Image struct {
	gorm.Model
	Data   string `gorm:"not null;check:data<>''"`
	UserID int    `gorm:"not null"`
}

type GetImagePayload struct {
	ID int `json:"id"`
}

type InsertImagePayload struct {
	UserID int    `json:"userID"`
	Data   string `json:"data"`
}
