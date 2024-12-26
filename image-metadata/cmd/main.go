package main

import (
	"image-metadata/cmd/api"
	"image-metadata/cmd/initializers"
	"log"
)

func main() {
	db, sqlDB := initializers.InitStorage()
	defer sqlDB.Close()

	// Start API server
	server := api.NewAPIServer(":8080", db)

	if err := server.Run(); err != nil {
		log.Panic("error running api server", err)
	}
}
