dependencies:
	@go mod tidy

build: dependencies
	@go build -ldflags "-X 'image-service/config.APIVersion=/api/v1'" -o bin/img-file-service cmd/main.go

gotest: build
	@go test -v ./...

httptest: build
	./bin/img-file-service &
	cd /tests/
	hurl test.hurl
	cd ..
	kill %1

run: build
	./bin/img-file-service
