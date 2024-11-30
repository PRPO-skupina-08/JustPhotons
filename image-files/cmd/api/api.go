package api

import (
	"log"
	"net/http"

	"github.com/go-chi/cors"
	"github.com/gorilla/mux"
)

type APIServer struct {
	addr string
}

func NewAPIServer(addr string) *APIServer {
	return &APIServer{
		addr: addr,
	}
}

// Runs the API server
func (s *APIServer) Run() error {
	// Creates router
	router := mux.NewRouter()

	// Register / add endpoints (services), controller == handler

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
