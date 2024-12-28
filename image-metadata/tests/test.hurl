POST http://localhost:8080/api/v1/metadata
{
    "imageId": 19,
    "rating": 5
}
HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "imageId": 23,
    "rating": 3
}
HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "imageId": 24,
    "rating": 3
}
HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "imageId": 22,
    "rating": 3
}
HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "imageId": 26,
    "rating": 0
}
HTTP 201

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
