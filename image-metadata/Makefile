dependencies:
	@go mod tidy

build: dependencies
	@go build -ldflags "-X 'image-service/config.APIVersion=/api/v1'" -o bin/img-file-service cmd/main.go

gotest: build
	@go test -v ./...

httptest: build
	@./bin/img-file-service & \
	server_pid=$$!; \
	{ cd ./tests/ && hurl test.hurl && [ "$$?" -eq 0 ] && echo "TESTING PASSED" || echo "TESTING FAILED"; } || true; \
	kill $$server_pid

migrate: build
	@go run cmd/migrate/migrate.go

run: build
	./bin/img-file-service
