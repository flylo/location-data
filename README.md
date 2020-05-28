# location-data

# NOTES
- If this was likely to become a large service that handles many different types of data
requests, then I would set it up with dagger2.
- Bigtable costs lots of money. Using Firestore.
- Using compute engine default instance for all service account shit
- Needs GOOGLE_APPLICATION_CREDENTIALS specified
- I don't love Immutables, but they're better than POJOs and setting up protobufs seemed like more effort
than was worth it for a take-home assignment
- [Indexing in firestore](https://firebase.google.com/docs/firestore/query-data/index-overview#single-field-indexes)
- [Indexing best practices](https://firebase.google.com/docs/firestore/query-data/index-overview#indexing_best_practices)
    - google suggests turning off auto-indexing for several specific use cases, none of which _seem_ to apply here
    - 500 writes per second per collection
    - 20k index entries per document (each field generates 2, one ascending one descending)
- If the fuzziness is really important, maybe something like Elasticsearch would be better, but this would have been a
more time-intensive infrastructure effort on my part.
- Docker is a pre-requisite

# Build
TODO: this isn't necessary
`gcloud builds submit --tag gcr.io/location-data-278514/location-data-image`

TODO: latest doesn't work
`gcloud config set compute/zone us-east1`
`gcloud container clusters create location-data-cluster --num-nodes=1`
`gcloud container clusters get-credentials location-data-cluster`
`kubectl create deployment location-data-service --image=gcr.io/location-data-278514/location-data-image:bbfe1b99fd5e8a41e994dbace8b6bbb1c187098f`
`kubectl expose deployment location-data-service --type=LoadBalancer --port 80 --target-port 8080`
`kubectl set image deployment/location-data-service location-data-image=gcr.io/location-data-278514/location-data-image:bbfe1b99fd5e8a41e994dbace8b6bbb1c187098f`


# Curl
`curl -X GET http://35.227.13.18:80/ping`
