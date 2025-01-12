kind load docker-image justphotons-coordinator:latest --name $KIND_CLUSTER_NAME
kind load docker-image justphotons-frontend:latest --name $KIND_CLUSTER_NAME
kind load docker-image justphotons-image-files:latest --name $KIND_CLUSTER_NAME
kind load docker-image justphotons-image-metadata:latest --name $KIND_CLUSTER_NAME
kind load docker-image justphotons-organisations:latest --name $KIND_CLUSTER_NAME
kind load docker-image justphotons-permission-check:latest --name $KIND_CLUSTER_NAME
kind load docker-image justphotons-users:latest --name $KIND_CLUSTER_NAME
