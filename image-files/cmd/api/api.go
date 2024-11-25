package main

import (
	"image-service/internal/store"
	"log"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
)

type application struct {
	config config
	store  store.Storage
}

type config struct {
	addr string
	db   dbConfig
}

type dbConfig struct {
	address            string
	maxOpenConnections int
	maxIdleConnections int
	maxIdleTime        string
}

func (app *application) mount() http.Handler {
	r := chi.NewRouter()

	r.Use(middleware.Recoverer)
	r.Use(middleware.Logger)
	r.Use(middleware.RealIP)
	r.Use(middleware.RequestID)

	// Set a timeout value on the request context (ctx), that will signal
	// through ctx.Done() that the request has timed out and further
	// processing should be stopped.
	r.Use(middleware.Timeout(60 * time.Second))

	r.Route("/v1", func(r chi.Router) {
		r.Get("/health", app.healthCheckHandler)
	})

	return r
}

func (app *application) run(mux http.Handler) error {
	srv := &http.Server{
		Addr:         app.config.addr,
		Handler:      mux,
		WriteTimeout: time.Second * 30, // Write should be more than read
		ReadTimeout:  time.Second * 10, // Read should be less than write
		IdleTimeout:  time.Minute,
	}

	log.Printf("Server has started at %s", app.config.addr)

	return srv.ListenAndServe()
}
