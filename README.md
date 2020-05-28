# location-data
An API for reading and writing banking transaction data.

## Usage

### Submit a visit
The following endpoint is exposed to submit a user transaction location: 
```
POST /users/{USER_UUID}/visits '{...}'
```

An example of how to use this in production is as follows:
```bash
curl -H 'Content-Type: application/json' -X POST -d '
{
  "merchant" : {
    "merchantId" : "c638f5e8-c256-4147-90e1-7973b3f2e3f2",
    "merchantName" : "Something"
  },
  "user" : {
    "userId" : "a5f72426-0d69-445d-89f2-6efabdd7f1f8"
  }
}' http://34.74.53.63/users/a5f72426-0d69-445d-89f2-6efabdd7f1f8/visits
```

### Retrieve a list of potential visits by userId and a search string
The following endpoint is exposed to fetch user transaction histories by userId, max lookback window, and an optional
search string:
```
GET /users/{USER_UUID}/visits?searchString={SEARCH_QUERY}?maxLookbackHrs={LOOKBACK_HRS}
```

An example of how to use this in production is as follows:
```bash
curl -X GET http://34.74.53.63/users/a5f72426-0d69-445d-89f2-6efabdd7f1f8/visits?searchString=Something&maxLookbackHrs=10
```

The max lookback window exists to allow for efficient filtering of visits.

### Retrieve a single visit by visitId
The following endpoint is exposed to fetch visits by their ID:
```
GET /visits/{VISIT_UUID}
```

An example of how to use this in production is as follows:
```bash
curl -X GET http://34.74.53.63/visits/33dd6314-e9fa-48d5-be9b-4b189f05e660
```

### Ping the service
The following endpoint allows you to quickly check if the service is deployed:
```
GET /ping
```

An example of how to use this in production is as follows:
```bash
curl -X GET http://34.74.53.63/ping
```
The user should receive a "PONG".

## CI/CD
Continuous Integration is done via [Google Cloud Build](https://cloud.google.com/cloud-build).
The configuration for build/test/deployment can be found [here](cloudbuild.yaml).

## Deployment
This service is deployed with [Google Kubernetes Engine](https://cloud.google.com/kubernetes-engine).
The cluster was built via the script at [`scripts/cluster-create.sh`](scripts/cluster-create.sh).
In order to run that script, the user will need the Google Cloud SDK installed (see below in "Local Development").

## Local Development
In order to develop in this repository, you will need the following tools set up locally:

- [Google Cloud SDK](https://cloud.google.com/sdk) should be installed, along with the `gcloud` CLI.
- [Java11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Docker](https://www.docker.com/) ([Docker for Mac](https://docs.docker.com/docker-for-mac/install/) is especially helpful)
- [Apache Maven 3](https://maven.apache.org/)
- Set the `GOOGLE_APPLICATION_CREDENTIALS` environment variable to the path of the GCP json credentials file 
([see documentation here](https://cloud.google.com/docs/authentication/getting-started)).
