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
- `GET /api/v1/images/{id}` - retrieves a specific image (for now it's just a 
    placeholder string)
- `POST /api/v1/images` - Create an image. Required fields: `data` and `user_id`

## Developer notes

### Testing, running and more

#### Database

To connect to the database (MariaDB), download MariaDB and use its shell. To
connect to the containerized database running on the Docker on IP 172.17.0.2,
run:
```bash
mariadb -h 172.17.0.2 -u root -p
```

`-p` will prompt for password, which was set when initializing the DBMS.

#### GORM Migrations

> TODO: It is perhaps a good practice to drop the table before running the 
migration, or else the database won't be instantiated. This has to be proven 
though.

To run the GORM's migration functionality, simply execute this in the project's:
root folder
```bash
go run cmd/migrate/migrate.go
```

#### Testing the Applicaton

To test the application (that is, the specific services of the application, in
this case there is only the `images` service), run this command:
```bash
make test
```

### The code itself

#### File Structure
- `/cmd/` - contains `main()`, the API logic and database migration logic
- `/config/` - mostly for retrieving data from environment variables
- `/db/` - database logic
- `/services/` - all the business logic
- `/utils/` - common functions (e.g. data validation, JSON parsing)

#### Call Chain

