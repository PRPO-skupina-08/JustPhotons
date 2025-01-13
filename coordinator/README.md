# Organisations microservice

## How to run

In `/organisations` directory run:
```bash
docker build -t coordinator .
docker run -p 8080:8080 coordinator
```

## API endpoints (/api/v1 +)

### /login

#### POST

Expects: `LoginEssentials`
Returns: JWT as `JwtResponse`

### /register

#### POST

Expects: `RegistrationEssentials`
Returns: JWT as `JwtResponse`

### /organisations

### GET

Expects: Valid JWT
Returns: All your organisaitions as `OrganisatoinEssentials`

### /organisations/:id
### GET

Expects: Valid id, valid JWT
Returns: organisations with id as `Organisation` in JWT matches.

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

### JwtResponse

```java
    private String token;

    private String error;
```


### Organisation

```java
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "You must specify organisations name")
    @Size(min = 1, max = 128)
    private String name;

    @NotBlank(message = "You must specify some description")
    @Size(min = 10, max = 1024)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organisation")
    private List<Album> albums = new ArrayList<>();
```

### OrganisationEssentials

```java
    private Long id;
    private String name;
```

### Album

```java
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "You must specify albums title")
    @Size(min = 1, max = 64, message = "Album title should be from 1 to 64 characters long.")
    private String title;

    @Size(min = 24, max = 24, message = "imgId is always 24 chars long")
    private String titleImage;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;
```
