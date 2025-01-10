package initializers

import (
	"database/sql"
	"permission-check/db"
	"log"
	"time"

	"gorm.io/gorm"
)

func InitStorage() (*gorm.DB, *sql.DB) {
	dbConn, err := db.NewMariaDB()
	if err != nil {
		log.Panic("Error connecting to database", err)
	}

	sqlDB, err := dbConn.DB()
	if err != nil {
		log.Panic("Something went wrong when creating a generic DB object", err)
	}
	if err := sqlDB.Ping(); err != nil {
		log.Panic("error when connecting to db:", err)
	}

	log.Println("Connected to DB")

	// Setup connection pool
	sqlDB.SetMaxIdleConns(10)
	sqlDB.SetMaxOpenConns(20) // 100
	sqlDB.SetConnMaxIdleTime(time.Minute)
	sqlDB.SetConnMaxLifetime(time.Hour)

	return dbConn, sqlDB
}
