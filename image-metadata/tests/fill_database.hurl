POST http://localhost:8080/api/v1/metadata
{
    "imageId": 8,
    "rating": 5
}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "imageId": 10,
    "rating": 3
}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "imageId": 11,
    "rating": 2
}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "imageId": 12,
    "rating": 3
}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "imageId": 14,
    "rating": 0
}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "imageId": 14,
    "rating": 15
}
HTTP 400

POST http://localhost:8080/api/v1/metadata
{
    "imageId": 0,
    "rating": 3
}
HTTP 400
