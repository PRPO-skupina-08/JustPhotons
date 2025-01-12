#!/bin/bash

for i in $(seq 1 1000);
do
    curl --location '57.153.83.62/api/v1/organisations' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwidXNlcm5hbWUiOiJjYmEiLCJpYXQiOjE3MzY1MjMyMzAsImV4cCI6MTczNjYyMzIzMH0.QIBIq6nA6s_aohmAq4UaAGqeX9u2k4iJdTgiZxCudJA'
    echo "sent!"
done