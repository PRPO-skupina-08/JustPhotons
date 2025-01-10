package main

import (
	"database/sql"
	"log"
	"web-service-gin/cmd/api"
	"web-service-gin/db"
	"web-service-gin/config"

	"github.com/go-sql-driver/mysql"
)

func main() {
	db, err := db.NewMySQLStorage(mysql.Config{
		User:                 config.Envs.DBUser,
		Passwd:               config.Envs.DBPassword,
		Addr:                 config.Envs.DBAddress,
		DBName:               config.Envs.DBName,
		Net:                  "tcp",
		AllowNativePasswords: true,
		ParseTime:            true,
	})
    if err != nil {
        log.Fatal(err)
    }
    initStorage(db)

	server := api.NewAPIServer(":8080", db)

	if err := server.Run(); err != nil {
		log.Fatal(err)
	}
}

func initStorage(db *sql.DB) {
    if err := db.Ping(); err != nil {
        log.Fatal(err)
    }

    log.Println("DB: Successfully connected!")
}
