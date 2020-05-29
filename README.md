# location-data
An API for reading and writing banking transaction data.

## Usage
The following provides an overview of the various endpoints.
For the complete OpenAPI Spec, [visit this link](http://34.74.53.63/swagger).

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
}' "http://34.74.53.63/users/a5f72426-0d69-445d-89f2-6efabdd7f1f8/visits"
```

### Retrieve a list of potential visits by userId and a search string
The following endpoint is exposed to fetch user transaction histories by userId, an optional max lookback window (in hours),
and an optional search string:
```
GET /users/{USER_UUID}/visits?searchString={SEARCH_QUERY}?maxLookbackHrs={LOOKBACK_HRS}
```

An example of how to use this in production is as follows:
```bash
curl -X GET "http://34.74.53.63/users/a5f72426-0d69-445d-89f2-6efabdd7f1f8/visits?searchString=Something&maxLookbackHrs=10"
```

The max lookback window exists to allow for efficient filtering of visits.

### Retrieve a single visit by visitId
The following endpoint is exposed to fetch visits by their ID:
```
GET /visits/{VISIT_UUID}
```

An example of how to use this in production is as follows:
```bash
curl -X GET "http://34.74.53.63/visits/33dd6314-e9fa-48d5-be9b-4b189f05e660"
```

### Ping the service
The following endpoint allows you to quickly check if the service is deployed:
```
GET /ping
```

An example of how to use this in production is as follows:
```bash
curl -X GET "http://34.74.53.63/ping"
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

## Miscellaneous Notes on Design Decisions
- We currently do fuzzy-matching in java, rather than in the persistence layer. This could be a bottleneck
if we have `userId`s with incredibly large transaction histories. To avoid issues, we allow for sub-setting of
the user visit history by a `maxLookbackHrs`. In practice, if this API is being used to do fraud detection,
then we don't need the entire user history. We'd likely just need the last few hours or days of transactions. We would
obviously want to verify this empirically, however, prior to putting something like this into production.
- The "fuzziness" parameter (maximum allowable [Levenshtein distance](https://en.wikipedia.org/wiki/Levenshtein_distance)
between merchant names) of the fuzzy-matching is currently configurable in the
[Dropwizard configuration file](location-data-service/src/main/resources/locationdataservice.yaml). We could also
put this into the API easily, but it seemed like doing so would un-necessarily expose the internals of the
service to an outside caller.
- I chose [Google Cloud Firestore](https://cloud.google.com/firestore) as our persistence layer for several reasons:
   1) It does automatic indexing of all non-map, non-array type fields
      (see [documentation](https://firebase.google.com/docs/firestore/query-data/index-overview#single-field-indexes)
      for details on Firestore's indexing support). This is incredibly helpful for us so that we can index
      our data on both the `visitId` and the `userId` in a single transaction. It also has a friendly pricing model
      for this type of project, and the ascending/descending indexing allows for performant ">=" and "<=" queries,
      which we need for the max-lookback.
   2) [Bigtable](https://cloud.google.com/bigtable) was WAY too expensive (~$500/month for the cheapest single-node deployment).
      I'd usually prefer to use Bigtable for a few reasons. Bigtable's lighting-fast range scan ability would help us subset the user
      visit history over time and keep the in-process fuzzy-matching logic from getting too computationally expensive.
      Also, the weaker consistency guarantees mean that we can have incredibly high throughput writes, which
      is usually good for feature stores (assuming we'd want an ML model or something to use this data).
   3) [Elasticsearch](https://www.elastic.co) seemed like overkill just to get some fuzzy-matching. If the `searchString`
      functionality became a core use-case, then I'd re-consider.
- The production Firestore database has a composite index set up on the fields `userId` and `timestampMillis` to support
joint sub-setting. The [Firestore Emulator](https://github.com/maximelebastard/firestore-emulator-docker) does
not support the creation of composite indexes, but it doesn't affect the tests.
- This repository is set up so that OpenAPI yaml specifications are generated programmatically from the API java resources. We
then compile those specs into java clients in the `location-data-client` package. Specifying all documentation and
client definition logic in the API itself cuts down on the number of moving pieces that need to be maintained.
- In the interest of time, I've prioritized integration testing, since all of the code is implicitly covered
in these tests. In a perfect world, classes like
[`FirestoreIO`](location-data-service/src/main/java/com/current/location/persistence/FirestoreIO.java)
would have their own independent unit tests.
- Merchant and user IDs are Strings since the prompt claims they come from a third-party provider. We can't assume them
to be UUIDs.
- I'm relying on the external IP address of the kubernetes cluster `LoadBalancer`. Ideally, we'd have this hosted in a
VPC with service discovery or DNS that would allow us to reach the service without calling the IP directly.
- Ideally we would be communicating with this service over HTTPS, possibly through a nginx proxy that could handle
SSL termination for us. This seemed like overkill for this particular project, however.
- If this was likely to become a large service that handled a variety of operations,
then I would set it up with compile-time dependency injection (using [dagger](https://github.com/google/dagger)).
