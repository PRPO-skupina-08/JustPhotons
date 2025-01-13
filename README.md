# *JustPhotons*

Bored with Google photos and all the other photo sharing platforms? 
Use *Just Photons* instead.

## Directory structure
Each top-level directory contains a code for a particular microservice. An exception here is `/frontend`, as it holds code for frontend, written in Angular.

Each Java microservice directory is structured in following way:
- `pom.xml` contains maven build properties and dependencies under `<finalName>organisations</finalName>` the name of built artefact is specified
- `/src/main/java/<package name>` contains three directories:
    - `/api/v1` - for managing API endpoints
    - `/entities` - ORM
    - `/services` - business logic
- `/src/main/resources` - contains `application.properties` for setting app-wide configurations and `data.sql` which is used to populate database on startup

Each Go microservice directory is structured in following way:
- `cmd` - code inside here runs the server itself
    - `api/api.go` - defines endpoints
    - `health/health.go` - code for the health checks
    - `initializers/initializers.go` - code for initializing the storage
    - `migrate/migrate.go` - code for database migrations
    - `main.go` - entry point
- `config` - configuration files
    - `constants.go` - for constants used service-wide
    - `env.go` - for environment variables
- `db/db.go` - database configuration files
- `services/<service>` - contains all code for a specific microservice
    - `routes.go` - code for registering and defining handlers for endpoints
    - `routes_test.go` - code for testing created handlers
    - `store.go` - code for interacting with the database

## Deployment

### How to deploy with docker 

#### Prerequisites

- docker installed on your machine
- a lot of patience for debugging our non-working code :)

#### Deployment

In a root folder execute:
```
docker compose -d up
```

And that's it :)

### How to deploy with kubernetes

#### Prerequisites

- kubernetes and kubectl installed on your machine (or on a public cloud)
- even more patience for debugging kubernetes :)

#### Deployment

first execute:
```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.3.0/deploy/static/provider/cloud/deploy.yaml
``` 
for adding an ingress controller.

```
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
```
for adding a metrics-server (if you don't already have one)

and finally you move to `/k8` folder and execute
```
kubectl apply -f .
```

that should be it :)

## OAS

Swagger OpenAPI specification can be found over a following url: `<application base domain>/swagger-ui/index.html`