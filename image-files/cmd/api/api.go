package api

import (
	"image-service/cmd/health"
	"image-service/config"
	"image-service/services/images"
	"log"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/cors"
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

// Runs the API server
func (s *APIServer) Run() error {
	// Creates router_prefix
	router_prefix := chi.NewRouter()
    router := chi.NewRouter()
    router_prefix.Mount(config.APIVersion, router)

	// Register / add endpoints (services), controller == handler
	imageStore := images.NewStore(s.db)            // prepare for dependency injection
	imagesHandler := images.NewHandler(imageStore) // create the controller and inject the dependency
	imagesHandler.CreateRoutes(router)

	// Healthcheck
	router.Get(config.Healthcheck, health.HealthCheckHandler)

	// For frontend.
	c := cors.New(cors.Options{
		AllowedOrigins:   []string{"http://localhost:5173"},
		AllowedMethods:   []string{"GET", "POST", "DELETE"},
		AllowedHeaders:   []string{"Authorization", "Content-Type"},
		AllowCredentials: true,
	})
	corsHandler := c.Handler(router_prefix)

	log.Printf("API server listening on port %s", s.addr)
	log.Printf("API endpoint base: %s%s", config.APIVersion, config.Subroute)
	log.Printf("Healthcheck: %s%s", config.APIVersion, config.Healthcheck)

	return http.ListenAndServe(s.addr, corsHandler)
}
