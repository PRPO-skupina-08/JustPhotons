package main

import (
	"image-service/cmd/initializers"
	"image-service/types"
)

func init() {
}

func main() {
    db, sqlDB := initializers.InitStorage()
	defer sqlDB.Close()

    db.AutoMigrate(&types.Image{})
}
