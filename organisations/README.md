# Organisations microservice

## How to run

In `/organisations` directory run:
```bash
docker build -t organisations .
docker run -p 8082:8082 organisations
```

## API endpoints

### /organisations

#### GET

Expects: /
Returns: List of all organisations as `OrganisationEssentials`.

#### POST

Expects: Body = valid (no id/albums) `Organisation`
Returns: created `Organisation`.

### /organisations/essentials/:orgId

### GET

Expects: Valid orgId
Returns: organisation with orgId as `OrganisationEssentials`.

### /organisations/:orgId

### GET

Expects: Valid orgId
Returns: organisation with orgId as `Organisation`.

### PUT

Expects: Valid orgId | valid body (don't send id or album here- it won't be updated, as albums table takes care of album ownership - change albums through `.../album` instead) (`Orgnisation`)
Returns: updated state as `Organisation`

### DELETE

Expects: Valid orgId
Returns: No content

## /organisations/:orgId/albums

### GET

Expects: Valid orgId, (optional: ?page=n&pageSize=m), defaults: page=0, pagesize=25
Returns: `List<Album>`

### POST

Expects: Valid orgId, valid body (`Album` w/o id and orgId)
Returns: `List<Album>`

## /organisations/:orgId/albums/:albumId

## PUT

Expects: Valid orgId and albumId, valid body (`Album` w/o id and orgId)
Returns: updated `Album`

## PUT

Expects: Valid orgId and albumId
Returns: No content

## Models

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