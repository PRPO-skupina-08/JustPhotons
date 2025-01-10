package types

import "gorm.io/gorm"

type MetadataStore interface {
	GetMetadataById(uint) (Metadata, *gorm.DB)
	GetAllMetadata(int, int, *string, uint, uint) ([]*Metadata, *gorm.DB)
	InsertMetadata(*Metadata) (*Metadata, *gorm.DB)
	DeleteMetadata(uint) (*gorm.DB)
	DeleteSpecificMetadata(*[]string) (*gorm.DB)
}

type Metadata struct {
	gorm.Model `swaggertype:"object" json:"-"`
	// ImageRef uint
	// Image    Image `gorm:"foreignKey:ImageRef"`
	ImageId uint `gorm:"not null;check:image_id <> '';check:image_id > 0;unique"`
	Rating  uint `gorm:"not null;check:rating <> '';check:rating >= 0;check:rating <= 5"`
}

// type Image struct {
// 	gorm.Model
// 	Filename string `gorm:"not null;check:filename<>''"`
// 	Data     []byte `gorm:"not null;check:data<>''"`
// }

type InsertMetadataPayload struct {
	ImageId uint `json:"image_id"`
	Rating  uint `json:"rating"`
}
