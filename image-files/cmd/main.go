package main

import (
	"image-service/cmd/api"
	"image-service/config"
	"image-service/db"
	"log"
)

func main() {
	dsn := config.GetDSN() // get the data source name
	dbConn, err := db.NewMariaDB(dsn)
	if err != nil {
		log.Panic("Error connecting to database", err)
	}
	log.Println("Connected to DB")

	sqlDB, err := dbConn.DB()
	if err != nil {
		log.Panic("Something went wrong when creating a generic DB object")
	}

	if err := sqlDB.Ping(); err != nil {
		log.Panic("error when connecting to db:", err)
	}
	log.Println("Pinged DB running on")

	// Start API server
	apiServer := api.NewAPIServer(":8080")
	if err := apiServer.Run(); err != nil {
		log.Panic("error running api server")
	}
}
