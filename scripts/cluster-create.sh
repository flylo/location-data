#!/bin/bash

gcloud config set compute/zone us-east1-c
gcloud container clusters create location-data-cluster --num-nodes=1 \
    --scopes https://www.googleapis.com/auth/datastore,https://www.googleapis.com/auth/devstorage.read_only,https://www.googleapis.com/auth/logging.write,https://www.googleapis.com/auth/monitoring,https://www.googleapis.com/auth/service.management.readonly,https://www.googleapis.com/auth/servicecontrol,https://www.googleapis.com/auth/trace.append
gcloud container clusters get-credentials location-data-cluster
kubectl create deployment location-data-service --image=gcr.io/location-data-278514/location-data-image:c0204c7456d7838968a2efe27feeaa0093ea65d7
kubectl expose deployment location-data-service --type=LoadBalancer --port 80 --target-port 8080
