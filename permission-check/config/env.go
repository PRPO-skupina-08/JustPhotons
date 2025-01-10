package config

import (
	"fmt"
	"os"

	"github.com/joho/godotenv"
)

type DBConfig struct {
	Address  string
	Port     string
	User     string
	Password string
	Name     string
}

func getENV(key, fallback string) string {
	if value, ok := os.LookupEnv(key); ok {
		return value
	}

	return fallback
}

func initDBConfig() DBConfig {
	godotenv.Load()

	return DBConfig{
		Address:  getENV("DB_ADDRESS", "172.17.0.2"),        // naslov, na katerem je podatkovna baza
		Port:     getENV("DB_PORT", "3306"),                 // port podatkovne baze
		User:     getENV("DB_USER", "image_files_user"),     // uporabniško ime
		Password: getENV("DB_PASSWORD", "Zelo_Mocno_Geslo"), // uporabniško geslo
		Name:     getENV("DB_NAME", "image_files"),          // ime podatkovne baze znotraj upravitelja podatkovnih baz
	}
}

func GetDSN() string {
	dbcfg := initDBConfig()
	return fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8mb4&parseTime=True&loc=Local",
		dbcfg.User,
		dbcfg.Password,
		dbcfg.Address,
		dbcfg.Port,
		dbcfg.Name,
	)
}
