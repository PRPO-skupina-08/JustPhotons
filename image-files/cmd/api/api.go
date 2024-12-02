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
	// Creates router
	router := chi.NewRouter()
    subrouter := chi.NewRouter()
    router.Mount("/api/v1", subrouter)

    // TODO: Test function
    subrouter.Get("/", func(w http.ResponseWriter, r *http.Request) {
        w.Write([]byte("Hello, World!"))
    })

	// Register / add endpoints (services), controller == handler
    imageStore := images.NewStore(s.db)
    imagesHandler := images.NewHandler(imageStore)
    imagesHandler.CreateRoutes(subrouter)

    // For frontend.
	c := cors.New(cors.Options{
		AllowedOrigins:   []string{"http://localhost:5173"},
		AllowedMethods:   []string{"GET", "POST", "DELETE"},
		AllowedHeaders:   []string{"Authorization", "Content-Type"},
		AllowCredentials: true,
	})
	corsHandler := c.Handler(router)

	log.Printf("API server listening on port %s", s.addr)

	return http.ListenAndServe(s.addr, corsHandler)
}
