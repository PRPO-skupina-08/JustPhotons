package db

import (
	"image-metadata/config"

	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

// dsn = data source name
func NewMariaDB() (*gorm.DB, error) {
	dsn := config.GetDSN() // get the data source name
	db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		return nil, err
	}

	return db, nil
}
