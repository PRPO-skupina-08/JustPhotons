package main

import (
	"image-metadata/cmd/initializers"
	"image-metadata/types"
)

func init() {
}

func main() {
	db, sqlDB := initializers.InitStorage()
	defer sqlDB.Close()

	db.AutoMigrate(&types.Metadata{})
}
