package api

import (
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
    db *gorm.DB
}

func NewAPIServer(addr string, db *gorm.DB) *APIServer {
	return &APIServer{
		addr: addr,
        db: db,
	}
}

// Runs the API server
func (s *APIServer) Run() error {
	// Creates prefixed_router
	prefixed_router := chi.NewRouter()
    router := chi.NewRouter()
    prefixed_router.Mount("/api/v1", router)

	// Register / add endpoints (services), controller == handler
    imageStore := images.NewStore(s.db) // prepare for dependency injection
    imagesHandler := images.NewHandler(imageStore) // create the controller and inject the dependency
    imagesHandler.CreateRoutes(router)

    // For frontend.
	c := cors.New(cors.Options{
		AllowedOrigins:   []string{"http://localhost:5173"},
		AllowedMethods:   []string{"GET", "POST", "DELETE"},
		AllowedHeaders:   []string{"Authorization", "Content-Type"},
		AllowCredentials: true,
	})
	corsHandler := c.Handler(prefixed_router)

	log.Printf("API server listening on port %s", s.addr)

	return http.ListenAndServe(s.addr, corsHandler)
}
