dependencies:
	@go mod tidy

build: dependencies
	@go build -o bin/img-file-service cmd/main.go

test:
	@go test -v ./...

run: build
	./bin/img-file-service
