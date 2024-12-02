dependencies:
	@go mod tidy

build: dependencies
	@go build -o bin/ecom cmd/main.go

test:
	@go test -v ./...

get:
	@go get ./...

run: build
	./bin/ecom
