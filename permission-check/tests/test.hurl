POST http://localhost:8080/api/v1/permissions
{
    "org_id": 19,
    "user_id": 5
}
HTTP 201

POST http://localhost:8080/api/v1/permissions
{
    "org_id": 19,
    "user_id": 5
}
HTTP 400

POST http://localhost:8080/api/v1/permissions
{
    "org_id": 23,
    "user_id": 3
}
HTTP 201

POST http://localhost:8080/api/v1/permissions
{
    "org_id": 24,
    "user_id": 3
}
HTTP 201

POST http://localhost:8080/api/v1/permissions
{
    "org_id": 22,
    "user_id": 3
}
HTTP 201

POST http://localhost:8080/api/v1/permissions
{
    "org_id": 26,
    "user_id": 0
}
HTTP 400

POST http://localhost:8080/api/v1/permissions
{
    "org_id": 0,
    "user_id": 26
}
HTTP 400

############################################
GET http://localhost:8080/api/v1/permissions
[Options]
output: -
HTTP 200
############################################

GET http://localhost:8080/api/v1/permissions
[Options]
output: -
[QueryStringParams]
org_id: 19
user_id: 5

GET http://localhost:8080/api/v1/permissions
[Options]
output: -
[QueryStringParams]
user_id: 3

DELETE http://localhost:8080/api/v1/permissions
[Options]
output: -
[QueryStringParams]
org_id: 19
user_id: 5

DELETE http://localhost:8080/api/v1/permissions
[Options]
output: -
[QueryStringParams]
user_id: 3
