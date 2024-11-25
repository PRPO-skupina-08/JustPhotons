package store

import (
	"context"
	"database/sql"
)

type Storage struct {
	Posts interface {
		Create(context.Context, *Post) error
		// GetByID(context.Context, int64) (*Post, error)
		// Delete(context.Context, int64) error
		// Update(context.Context, *Post) error
		// GetUserFeed(context.Context, int64, PaginatedFeedQuery) ([]PostWithMetadata, error)
	}
}

func NewStorage(db *sql.DB) Storage {
	return Storage{
		Posts: &ImageStore{db},
	}
}
