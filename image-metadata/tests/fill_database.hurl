POST http://localhost:8080/api/v1/metadata
{
    "image_id": 8,
    "rating": 5,
    "album_id": 1
}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "image_id": 10,
    "rating": 3,
    "album_id": 3

}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "image_id": 11,
    "rating": 2,
    "album_id": 10

}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "image_id": 12,
    "rating": 3,
    "album_id": 15

}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "image_id": 14,
    "rating": 0,
    "album_id": 20

}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "image_id": 14,
    "rating": 15,
    "album_id": 30

}
HTTP 400

POST http://localhost:8080/api/v1/metadata
{
    "image_id": 14,
    "rating": 0,
    "album_id": 30

}
HTTP 400

POST http://localhost:8080/api/v1/metadata
{
    "image_id": 0,
    "rating": 3,
    "album_id": 40

}
HTTP 400

POST http://localhost:8080/api/v1/metadata
{
    "image_id": 10,
    "rating": 3,
    "album_id": 0

}
HTTP 400
