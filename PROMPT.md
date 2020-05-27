# Location API

To help reduce fraud, Current partners with a location service provider to ensure Current cardholders are physically proximate to where their card is being used. For example, if we know a user is in New York, and their card is used at a gas station in North Carolina, we know we should decline the transaction until the user confirms this transaction is expected.

## Assignment Overview

Using any technologies you choose, design and host an API that accepts mapped user locations (visits) and can be queried with a userId and search string. The search is expected to be performed in real-time so the p99 response time should be below one second.

### Schema

```
Visit {
  visitId: string
  timestamp: int64
  merchant: Merchant
  user: User
}
```

```
User {
  userId: string
}
```

```
Merchant {
  merchantId: string
  merchantName: string
}
```

## Supported Operations

### Submit a visit
```
POST /users/{userId}/visits
```
_Example_
```
POST /users/a955cac6-fb81-4eb9-a3b5-b87d28b86796/visits
{
  merchant: {
    merchantId: 'fa9d770b-70a7-471c-9897-e94515fc11b3',
    merchantName: 'Starbucks'
  },
  user: {
    userId: 'e2d42a60-81c4-44b0-b968-6f4cbc52444f'
  }
}

Returns:
{
  visitId: '7209143d-c742-4afa-a0ac-ca7c1b8cc39d',
  timestamp: 1590441350768
}
```

### Retrieve a list of potential visits by userId and a search string
```
GET /users/{userId}/visits?searchString=X
```
_Example_
```
GET /users/e2d42a60-81c4-44b0-b968-6f4cbc52444f/visits?searchString=STARBUCKS%231249

Returns:
[{
  visitId: '7209143d-c742-4afa-a0ac-ca7c1b8cc39d',
  timestamp: 1590441350768,
  merchant: {
    merchantId: 'fa9d770b-70a7-471c-9897-e94515fc11b3',
    merchantName: 'Starbucks'
  },
  user: {
    userId: 'e2d42a60-81c4-44b0-b968-6f4cbc52444f'
  }
}]
```
  
### Retrieve a single visit by visitId
```
GET /visits/{visitId}
```
_Example_
```
GET /visits/7209143d-c742-4afa-a0ac-ca7c1b8cc39d

Returns:
{
  visitId: '7209143d-c742-4afa-a0ac-ca7c1b8cc39d',
  timestamp: 1590441350768,
  merchant: {
    merchantId: 'fa9d770b-70a7-471c-9897-e94515fc11b3',
    merchantName: 'Starbucks'
  },
  user: {
    userId: 'e2d42a60-81c4-44b0-b968-6f4cbc52444f'
  }
}
```

## Additional Requirements

1. Host the API in a publically accessible way
2. Persist the data on a non-ephemeral store
3. The search should have some fuzziness built in (preferably configurable)
```
endy should match with WENDY'S
```

## Bonus

1. Provide tests
2. Provide instructions to build and test locally

## Delivery

Once complete, please attach @finco-trevor as a git project collaborator. Please make sure the link to your endpoint is provided in the README.
