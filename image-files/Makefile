dependencies:
	@go mod tidy

build: dependencies
	@go build -ldflags "-X 'image-service/config.APIVersion=/api/v1'" -o bin/img-file-service cmd/main.go

test:
	@go test -v ./...

run: build
	./bin/img-file-service
