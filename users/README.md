# Organisations microservice

## How to run

In `/organisations` directory run:
```bash
docker build -t users .
docker run -p 8081:8081 users
```

## API endpoints (/v1 +)

### /login

#### POST

Expects: `LoginEssentials`
Returns: JWT as `string`

### /register

#### POST

Expects: `RegistrationEssentials`
Returns: JWT as `string`

### /users/id

### GET

Expects: Valid JWT
Returns: Your id as `number`

### /users/:id
### GET

Expects: Valid id
Returns: user with id as `UserEssentials`.

## Models

### Login Essentials

```java
    @Email(message = "valid email please")
    private String email;

    @Size(min = 8, max = 64, message = "password should be between 8 and 64 characters long")
    private String password;
```

### RegistrationEssentials

```java
    @NotBlank
    private String username;

    @Email(message = "valid email please")
    private String email;

    @Size(min = 8, max = 64, message = "password should be between 8 and 64 characters long")
    private String password;
```

### UserEssentials

```java
    private Long id;

    private String username;

    private String email;
```