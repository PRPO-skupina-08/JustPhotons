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
- `GET /api/v1/permissions/{id}` - retrieves a specific permissions entry based
on its ID.
- `GET /api/v1/permissions?...` - retrieves many permissions entries. Can have
query parameters.
- `POST /api/v1/permissions` - create permissions.
- `DELETE /api/v1/permissions/{id}` - delete permissions entry based on its ID.
- `DELETE /api/v1/permissions?...` - delete many permissions entries. Can have
query parameters.

These are the available query parameters for the functions that allow them:
- `limit` - integer - the maximum amount of entries to retrieve.
- `offset` - integer - the amount of entries at the start to leave out.
- `org_id` - integer - the ID of the organization to which the permission 
belongs.
- `user_id` - integer - the ID of the user to which the permission belongs.

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
- `USE <database name>`
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

> ***NOTE***: database migrations run every time the server is started. Database
> migrations do not delete data. So, whenever changing the structures in
> `types/types.go`, be sure to drop the table, manually or using `migrate.go`
> with procedure described below

##### Advanced `migrate.go` functions

The `migrate.go` program has more functionalities for assisting migrations 
management. It can be used with the flag `-action <action>`. Omitting this flag
is the same as running `-action migrate`, but you can also run `-action drop` 
to drop the currently existing table in the database.

#### Testing the Applicaton
For testing, **make sure you have [Hurl](https://hurl.dev/) installed**.

To test the application (that is, the specific services of the application, in
this case there is only the `permissions` service), run this command:
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
