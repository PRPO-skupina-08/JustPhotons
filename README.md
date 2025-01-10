# *JustPhotons*

Bored of Google photos and all the other photo sharing platforms? 
Use *Just Photons* instead.

## directory structure
Each top-level directory contains a code for a particular microservice. An exception here is `/frontend`, as it holds code for frontend, written in Angular.
Each microservice directory is structured in following way:
- `pom.xml` contains maven build properties and dependencies under `<finalName>organisations</finalName>` the name of built artefact is specified
- `/src/main/java/<package name>` contains three directories:
    - `/api/v1` - for managing api endpoints
    - `/entities` - ORM
    - `/services` - buisness logic
- `/src/main/resources` - contains `application.properties` for setting app-wide configurations and `data.sql` which is used to populate database on startup

## Deployment

### Prerequisites

- docker installed on your machine
- a lot of patience for debugging our non-working code :)

### How to deploy?

In a root folder execute:
```
docker compose -d up
```

And that's it :)

## OAS

Swagger OpenAPI specification can be found over a following url: `<application base domain>/swagger-ui/index.html`