# IMAGE-FILE MICROSERVICE

## Running

### Prerequisites
Make sure you have a MariaDB Docker container running on 172.17.0.x:3306 (this
should be the default address).

You can change the address, port, and database credentials using the `.env`
file. To see the default settings used in case no environment variables are 
used, see the `initDBConfig()` function inside `./config/env.go`

### Running
Make sure you have [Go](https://go.dev/) installed. This program utilizes 
Makefile for running and compilation.

You can run the program with the following command:

```bash
make run
```

## API (REST)
Due to early development phase of this app, the current API is as follows:
- `GET /api/v1/metadata/{id}` - retrieves a specific metadata entry based on its
ID.
- `GET /api/v1/metadata?...` - retrieves many metadata entries. Can have query
parameters.
- `POST /api/v1/metadata` - create metadata. Required fields: `image_id` and
`rating`.
- `DELETE /api/v1/metadata/{id}` - delete metadata entry based on its ID.
- `DELETE /api/v1/metadata?...` - delete many metadata entries. Can have query
parameters.

These are the available query parameters for the functions that allow them:
- `limit` - integer - the maximum amount of entries to retrieve.
- `offset` - integer - the amount of entries at the start to leave out.
- `sort` - string - the sorting parameters. Pattern: `<field>:<order>[,]...`
- `image_id` - integer - the ID of the image to which the metadata belongs.
- `rating` - integer - rating between 0 and 5 (inclusive)

## Developer notes

### Testing, running and more

#### Database

To connect to the database (MariaDB), download MariaDB and use its shell. To
connect to the containerized database running on the Docker on IP 172.17.0.2,
run:

```bash
mariadb -h 172.17.0.2 -u root -p
```

Useful commands:
- `USE image_files`
- `SHOW tables`
- `SELECT * from <table>`
- `DROP TABLE <table>`

`-p` will prompt for password, which was set when initializing the DBMS.

#### GORM Migrations

To run the GORM's migration functionality, use this Make command:

```bash
make migrate
```

This will run `cmd/migrate/migrate.go` program, that is tasked with performing
database migrations.

##### Advanced `migrate.go` functions

The `migrate.go` program has more functionalities for assisting migrations 
management. It can be used with the flag `-action <action>`. Omitting this flag
is the same as running `-action migrate`, but you can also run `-action drop` 
to drop the currently existing table in the database.

#### Testing the Applicaton
For testing, **make sure you have [Hurl](https://hurl.dev/) installed**.

To test the application (that is, the specific services of the application, in
this case there is only the `images` service), run this command:
```bash
make test
```

### The code itself

#### File Structure
- `/cmd/` - contains `main()`, the API logic and database migration logic,
- `/config/` - mostly for retrieving data from environment variables; constants.
- `/db/` - database logic.
- `/services/` - all the business logic.
- `/utils/` - common functions (e.g. data validation, JSON parsing).
