dependencies:
	@go mod tidy

build: dependencies
	@go build -ldflags "-X 'image-service/config.APIVersion=/api/v1'" -o bin/img-metadata-service cmd/main.go

gotest: build
	@go test -v ./...

httptest: build
	@./bin/img-metadata-service & \
	server_pid=$$!; \
	{ cd ./tests/ && hurl test.hurl && [ "$$?" -eq 0 ] && echo "TESTING PASSED" || echo "TESTING FAILED"; } || true; \
	kill $$server_pid

test: gotest httptest
	@echo "Tests completed."

migrate: build
	@go run cmd/migrate/migrate.go

swagger: build
	@swag f
	@swag init -d "./cmd,./services/metadata,./types,./utils" -g api/api.go -o cmd/docs

run: build swagger
	./bin/img-metadata-service
