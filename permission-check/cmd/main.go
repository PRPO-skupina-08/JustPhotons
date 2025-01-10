package main

import (
	"log"
	"permission-check/cmd/api"
	"permission-check/cmd/initializers"
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
