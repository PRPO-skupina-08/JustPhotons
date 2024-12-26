package types

import "gorm.io/gorm"

type MetadataStore interface {
	GetMetadataById(id uint) (md Metadata, result *gorm.DB)
	InsertMetadata(md *Metadata) (result *gorm.DB)
	DeleteMetadata(id uint) (result *gorm.DB)
}

type Metadata struct {
	gorm.Model
	// ImageRef uint
	// Image    Image `gorm:"foreignKey:ImageRef"`
	ImageId uint
	Rating  uint
}

// type Image struct {
// 	gorm.Model
// 	Filename string `gorm:"not null;check:filename<>''"`
// 	Data     []byte `gorm:"not null;check:data<>''"`
// }
