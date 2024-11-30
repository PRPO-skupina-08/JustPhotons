package db

import (
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

// dsn = data source name
func NewMariaDB(dsn string) (*gorm.DB, error) {
	db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		return nil, err
	}

	return db, nil
}
