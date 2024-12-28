package api

import (
	"image-metadata/config"
	"image-metadata/services/metadata"
	"log"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/cors"
	httpSwagger "github.com/swaggo/http-swagger"
    _ "image-metadata/cmd/docs"

	"gorm.io/gorm"
)

/**
 * reason to wrap this is so that we can implement interfaces and
 * our own methods on top of this struct
 */
type APIServer struct {
	addr string
	db   *gorm.DB
}

func NewAPIServer(addr string, db *gorm.DB) *APIServer {
	return &APIServer{
		addr: addr,
		db:   db,
	}
}

//	@title			Swagger Image Metadata API
//	@version		0.1.0
//	@description	This is a microservice for managing images' metadata

//	@BasePath	/api/v1
func (s *APIServer) Run() error {
	// Creates router_prefix
	router_prefix := chi.NewRouter()
	router := chi.NewRouter()
	router_prefix.Mount(config.APIVersion, router)

	// Register / add endpoints (services), controller == handler
	metadataStore := metadata.NewStore(s.db)              // prepare for dependency injection
	metadataHandler := metadata.NewHandler(metadataStore) // create the controller and inject the dependency
	metadataHandler.CreateRoutes(router)

    router.Mount("/docs", httpSwagger.WrapHandler)

	// For frontend.
	c := cors.New(cors.Options{
		AllowedOrigins:   []string{"http://localhost:5173"},
		AllowedMethods:   []string{"GET", "POST", "DELETE"},
		AllowedHeaders:   []string{"Authorization", "Content-Type"},
		AllowCredentials: true,
	})
	corsHandler := c.Handler(router_prefix)

	log.Printf("API server listening on port %s", s.addr)

	return http.ListenAndServe(s.addr, corsHandler)
}
