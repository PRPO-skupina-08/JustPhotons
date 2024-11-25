package store

import (
	"context"
	"database/sql"

	"github.com/lib/pq"
)

type Post struct {
	ID        int64    `json:"id"`
	Content   string   `json:"content"`
	Title     string   `json:"title"`
	UserID    int64    `json:"user_id"`
	Tags      []string `json:"tags"`
	CreatedAt string   `json:"created_at"`
	UpdatedAt string   `json:"updated_at"`
}

type ImageStore struct {
	db *sql.DB
}

func (s *ImageStore) Create(ctx context.Context, post *Post) error {
	query := `
    INSERT INTO images (content, title, user_id, tags)
    VALUES ($1, $2, $3, $3) RETURNING id, created_at, updated_at
    `

	err := s.db.QueryRowContext(
        ctx, 
        query,
        post.Content,
        post.Title,
        post.UserID,
        post.Tags,
        pq.Array(post.Tags),
    ).Scan(
        &post.ID,
        &post.CreatedAt,
        &post.UpdatedAt,
    )

	// if err != nil {
	//     return err
	// }
	//
	// return nil

	return err
}
