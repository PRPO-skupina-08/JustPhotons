package main

import (
	"flag"
	"fmt"
	"os"
	"permission-check/cmd/initializers"
	"permission-check/types"

	"gorm.io/gorm"
)

type action func(db *gorm.DB)

func run_action(actionName *string) {
	if actionName == nil {
		fmt.Fprintf(os.Stderr, "Error: something went horribly wrong if this pointer is nil!\n")
		return
	}

	// Initialize the database
	db, sqlDB := initializers.InitStorage()
	defer sqlDB.Close()

	// Define a map that associates strings with action functions
	actions := map[string]action{
		"":        migrate,
		"migrate": migrate,
		"drop":    drop,
	}

	// Get the action function based on the actionName
	action, exists := actions[*actionName]
	if !exists {
		fmt.Fprintf(os.Stderr, "Error: action '%s' does not exist\n", *actionName)
		flag.PrintDefaults()
		return
	}

	// Run the action
	action(db)
}

func migrate(db *gorm.DB) {
	db.AutoMigrate(&types.Permission{})
}

func drop(db *gorm.DB) {
	db.Migrator().DropTable(&types.Permission{})
}

func main() {
	action := flag.String("action", "", "Action to perform. Allowed actions: `migrate` (runs when flag isn't present), `drop`")
	flag.Parse()

	run_action(action)
}
