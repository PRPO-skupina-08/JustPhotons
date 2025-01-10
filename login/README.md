# LOGIN (USER) MICROSERVICE

This is a microservice that handles user register and login. In the future, this
it will offer extended functionality for managing users

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
- `POST /api/v1/register` - register with `firstName`, `lastName`, `email` and
    `password`. Email and password are validated.
- `POST /api/v1/login` - login with `email` and `password`.
