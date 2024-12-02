package types

import "gorm.io/gorm"

type ImageStore interface {
	GetImageById(id int) (img Image, result *gorm.DB)
	InsertImage(img *Image) (result *gorm.DB)
	// GetUserByEmail(email string) (*Image, error)
	// GetUserByID(id int) (*Image, error)
	// CreateUser(Image) error
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
