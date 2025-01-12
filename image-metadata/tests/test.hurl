POST http://localhost:8080/api/v1/metadata
{
    "image_id": 19,
    "rating": 5,
    "album_id": 6
}
HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "image_id": 23,
    "rating": 3,
    "album_id": 30

}
HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "image_id": 24,
    "rating": 3,
    "album_id": 60

}
HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "image_id": 22,
    "rating": 3,
    "album_id": 2

}
HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "image_id": 26,
    "rating": 1,
    "album_id": 1

}
HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "image_id": 26,
    "rating": 0,
    "album_id": 1

}
HTTP 400

############################################
GET http://localhost:8080/api/v1/metadata
[Options]
output: -
HTTP 200
############################################

DELETE http://localhost:8080/api/v1/metadata
[Options]
output: -
[QueryStringParams]
image_id: 19
HTTP 204

DELETE http://localhost:8080/api/v1/metadata
[Options]
output: -
[QueryStringParams]
image_id: 26
HTTP 204

DELETE http://localhost:8080/api/v1/metadata
[Options]
output: -
[QueryStringParams]
rating: 3
HTTP 204
