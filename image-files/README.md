# IMAGE-FILE MICROSERVICE



## Running

### Prerequisites
Make sure you have a MariaDB Docker container running on 172.17.0.x:3306 (this
should be the default address).

You can change the address, port, and database credentials using the `.env`
file. To see the default settings used in case no environment variables are 
used, see the `initDBConfig()` function inside `./config/env.go`

### Running
Make sure you have go installed. To run this microservice (for now, not 
containerized), you have two options:

First: run the following commands:
```bash
go mod tidy
go run ./cmd/main.go
```

Second (**make**): if you have Make installed, you can run the following:
```bash
make run
```

Either block of code you decide to use will start the application on the
default port of 3306

## API (REST)
Due to early development phase of this app, the current API is as follows:
- `GET /api/v1/images/{id}` - retrieves a specific image (for now its just a 
    placeholder string)
- `POST /api/v1/images` - Create an image. Required fields: `data` and `user_id`
