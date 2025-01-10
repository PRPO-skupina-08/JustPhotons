# Build the application from source
FROM golang:1.23 AS build-stage

WORKDIR /app

COPY go.mod go.sum Makefile ./
COPY bin/ ./bin/ 
COPY cmd/ ./cmd/ 
COPY config/ ./config/
COPY db/ ./db/
COPY services/ ./services/
COPY types/ ./types/
COPY utils/ ./utils/
COPY tests/ ./tests/

RUN CGO_ENABLED=0 GOOS=linux make build

# Run the tests in the container
FROM build-stage AS run-test-stage
RUN make gotest

# Deploy the application binary into a lean image
FROM gcr.io/distroless/base-debian11 AS build-release-stage

WORKDIR /

COPY --from=build-stage /app/bin/img-metadata-service /img-metadata-service

EXPOSE 8080

USER nonroot:nonroot

ENTRYPOINT ["/img-metadata-service"]
