GET http://localhost:8080/api/v1/images
[QueryStringParams]
limit: 2
offset: 2
HTTP 200
[Asserts]
jsonpath "$" count == 2

GET http://localhost:8080/api/v1/images
HTTP 200
[Captures]
id: jsonpath "$.[-1:]['ID']" nth 0
[Asserts]
jsonpath "$" count <= 10

GET http://localhost:8080/api/v1/images/{{id}}
HTTP 200
[Asserts]

GET http://localhost:8080/api/v1/images/1424
HTTP 404

POST http://localhost:8080/api/v1/images/
file,payload.json;
HTTP 201
[Captures]
postid: jsonpath "$['ID']"
postdata: jsonpath "$['Data']"

GET http://localhost:8080/api/v1/images/{{postid}}
HTTP 200
[Asserts]
jsonpath "$['Data']" == {{postdata}}

DELETE http://localhost:8080/api/v1/images/{{postid}}
HTTP 204

DELETE http://localhost:8080/api/v1/images/{{postid}}
HTTP 404
