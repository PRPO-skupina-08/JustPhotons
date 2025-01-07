POST http://localhost:8080/api/v1/metadata
{
    "org_id": 8,
    "user_id": 5
}
POST http://localhost:8080/api/v1/metadata
{
    "org_id": 8,
    "user_id": 6
}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "org_id": 10,
    "user_id": 3
}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "org_id": 11,
    "user_id": 2
}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "org_id": 12,
    "user_id": 3
}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "org_id": 14,
    "user_id": 0
}
# HTTP 201

POST http://localhost:8080/api/v1/metadata
{
    "org_id": 14,
    "user_id": 15
}
HTTP 400

POST http://localhost:8080/api/v1/metadata
{
    "org_id": 0,
    "user_id": 3
}
HTTP 400
