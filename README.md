# Customer Reward API

A Spring Boot API that records customer transactions and calculates reward points.

## Table of Contents
- Overview
- Features
- Tech
- Getting Started
- API Endpoints
- Examples (Postman bodies)
- Error Handling
- Testing
- Notes

## Overview
Service accepts transactions and computes reward points per business rules:
- 1 point for each dollar spent over 50
- 2 points for each dollar spent over 100 (so spending $120 = (20*2)+(50*1) = 90)

## Features
- CRUD for transactions (create and read)
- Reward calculation per customer
- Monthly reward endpoints
- DTOs for API responses
- Validation and global exception handling
- Unit tests with Mockito

## Tech
- Java 17
- Spring Boot 3.x
- Spring Data JPA with H2 (configurable)
- JUnit 5, Mockito

## Getting started
Build:
```
./mvnw clean package
```
Run:
```
java -jar target/customer-reward-0.0.1-SNAPSHOT.jar
```
Default port 8080. Configure datasource in `src/main/resources/application.properties`.

## API Endpoints

POST /api/transactions
- Create a transaction.
- Body JSON:
```json
{
  "customerId": 42,
  "amount": 120.00,
  "date": "2025-09-05T14:30:00"
}
```

GET /api/customers/{customerId}/transactions
- Get all transactions for a customer.

GET /api/customers/{customerId}/transactions?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
- Get transactions in date range.

GET /api/customers/{customerId}/rewards
- Get all-time rewards summary for a customer.

GET /api/customers/{customerId}/rewards?month=YYYY-MM
- Get rewards for a specific month. Month must be `YYYY-MM`.

GET /api/rewards?month=YYYY-MM
- Get monthly rewards for all customers.

## Postman examples

1) Create transaction
POST http://localhost:8080/api/transactions
Body (JSON):
{
  "customerId": 42,
  "amount": 120.00,
  "date": "2025-09-05T14:30:00"
}

2) Get monthly rewards for customer
GET http://localhost:8080/api/customers/42/rewards?month=2025-09


### Sample Responses

**POST /api/transactions**  
Response 201 Created:
```json
{
  "transactionId": 101,
  "customerId": 42,
  "amount": 120.00,
  "date": "2025-09-05T14:30:00",
  "points": 90
}
```

**GET /api/customers/{customerId}/transactions**  
Response 200 OK:
```json
[
  {
    "transactionId": 101,
    "customerId": 42,
    "amount": 120.00,
    "date": "2025-09-05T14:30:00",
    "points": 90
  },
  {
    "transactionId": 102,
    "customerId": 42,
    "amount": 75.00,
    "date": "2025-09-07T10:15:00",
    "points": 25
  }
]
```

**GET /api/customers/{customerId}/transactions?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD**  
Response 200 OK (filtered by date range):
```json
[
  {
    "transactionId": 102,
    "customerId": 42,
    "amount": 75.00,
    "date": "2025-09-07T10:15:00",
    "points": 25
  }
]
```

**GET /api/customers/{customerId}/rewards**  
Response 200 OK:
```json
{
  "customerId": 42,
  "month": null,
  "totalPoints": 115,
  "transactions": [
    { "transactionId": 101, "amount": 120.00, "date": "2025-09-05T14:30:00", "points": 90 },
    { "transactionId": 102, "amount": 75.00, "date": "2025-09-07T10:15:00", "points": 25 }
  ],
  "monthlyBreakdown": [
    { "month": "2025-09", "points": 115 }
  ]
}
```

**GET /api/customers/{customerId}/rewards?month=YYYY-MM**  
Response 200 OK:
```json
{
  "customerId": 42,
  "month": "2025-09",
  "totalPoints": 115,
  "transactions": [
    { "transactionId": 101, "amount": 120.00, "date": "2025-09-05T14:30:00", "points": 90 },
    { "transactionId": 102, "amount": 75.00, "date": "2025-09-07T10:15:00", "points": 25 }
  ]
}
```

**GET /api/rewards?month=YYYY-MM**  
Response 200 OK:
```json
[
  {
    "customerId": 42,
    "month": "2025-09",
    "totalPoints": 115,
    "transactions": [
      { "transactionId": 101, "amount": 120.00, "date": "2025-09-05T14:30:00", "points": 90 },
      { "transactionId": 102, "amount": 75.00, "date": "2025-09-07T10:15:00", "points": 25 }
    ]
  },
  {
    "customerId": 43,
    "month": "2025-09",
    "totalPoints": 60,
    "transactions": [
      { "transactionId": 103, "amount": 110.00, "date": "2025-09-09T12:00:00", "points": 60 }
    ]
  }
]
```

## Error handling
Errors return `ErrorResponse` with timestamp, status, error, message, and errors list.

## Testing
Run:
```
./mvnw test
```




## HTTP API

Base path: `/api`

### Endpoints

| Method | Path | Query | Description | Success | Errors |
|---|---|---|---|---|---|
| GET | `/customers/{customerId}/rewards` | none | All-time rewards for a customer | `200 OK` with `RewardSummaryDTO` | `404 Not Found`, `500 Internal Server Error` |
| GET | `/customers/{customerId}/rewards` | `month=yyyy-MM` | Rewards for a specific month | `200 OK` with `RewardSummaryDTO` | `400 Bad Request` invalid `month` |
| GET | `/rewards` | `month=yyyy-MM` | Monthly rewards for all customers | `200 OK` with `RewardSummaryDTO[]` | `400 Bad Request` invalid `month` |
| POST | `/transactions` | none | Create a transaction | `201 Created` with `TransactionDTO` | `400 Bad Request` validation failure |
| GET | `/customers/{customerId}/transactions` | none | List transactions for customer | `200 OK` with `TransactionDTO[]` | `404 Not Found` if customer unknown |
| GET | `/customers/{customerId}/transactions` | `startDate=yyyy-MM-dd&endDate=yyyy-MM-dd` | List transactions in date range | `200 OK` with `TransactionDTO[]` | `400 Bad Request` invalid dates |

### JSON Schemas

`RewardSummaryDTO`:
```json
{
  "customerId": 1,
  "totalPoints": 120,
  "transactionCount": 3
}
```

`TransactionDTO`:
```json
{
  "transactionId": 10,
  "customerId": 1,
  "amount": 100.0,
  "date": "2024-07-01"
}
```

### Request/Response Examples

**Create Transaction**
Request:
```http
POST /api/transactions
Content-Type: application/json

{
  "customerId": 1,
  "amount": 100.0,
  "date": "2024-07-01"
}
```
Response:
```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "transactionId": 10,
  "customerId": 1,
  "amount": 100.0,
  "date": "2024-07-01"
}
```

**Get All-time Rewards**
```http
GET /api/customers/1/rewards
```
Response `200 OK`:
```json
{
  "customerId": 1,
  "totalPoints": 120,
  "transactionCount": 3
}
```

**Get Monthly Rewards**
```http
GET /api/customers/1/rewards?month=2024-07
```
Response `200 OK`:
```json
{
  "customerId": 1,
  "totalPoints": 90,
  "transactionCount": 2
}
```

**Error Payload**
On errors the API returns:
```json
{
  "timestamp": "2024-07-01T00:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": ["month: must match ^\\d{4}-\\d{2}$"]
}
```

### Build and Test

```bash
# From repo root
./mvnw -q -DskipTests=false clean test
```

## Responses

### Create transaction
**POST** `/api/transactions`

**Request body**
```json
{
  "customerId": 42,
  "amount": 120.00,
  "date": "2025-09-05T14:30:00"
}
```

**Response 201**
```json
{
  "transactionId": 10,
  "customerId": 42,
  "amount": 120.0,
  "date": "2025-09-05T14:30:00",
  "rewardPoints": 90
}
```

**Error 400 (validation)**
```json
{
  "timestamp": "2025-09-11T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    "transactionDTO.amount: must be positive"
  ]
}
```

### Get transactions for a customer
**GET** `/api/customers/{customerId}/transactions`

**Response 200**
```json
[
  {
    "transactionId": 10,
    "customerId": 42,
    "amount": 120.0,
    "date": "2025-09-05T14:30:00",
    "rewardPoints": 90
  },
  {
    "transactionId": 11,
    "customerId": 42,
    "amount": 60.0,
    "date": "2025-09-06T12:00:00",
    "rewardPoints": 10
  }
]
```

### Get transactions for a customer by date range
**GET** `/api/customers/{customerId}/transactions?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD`

**Response 200**
```json
[
  {
    "transactionId": 10,
    "customerId": 42,
    "amount": 120.0,
    "date": "2025-07-01T09:00:00",
    "rewardPoints": 90
  }
]
```

**Error 400 (invalid range)**
```json
{
  "timestamp": "2025-09-11T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "startDate must be before or equal to endDate",
  "errors": []
}
```

**Error 404 (no data)**
```json
{
  "timestamp": "2025-09-11T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found or no transactions in range",
  "errors": []
}
```

### Get monthly rewards for a customer
**GET** `/api/customers/{customerId}/rewards?month=YYYY-MM`

**Response 200**
```json
{
  "customerId": 42,
  "month": "2025-09",
  "totalRewardPoints": 180,
  "transactions": [
    {
      "transactionId": 10,
      "customerId": 42,
      "amount": 120.0,
      "date": "2025-09-05T14:30:00",
      "rewardPoints": 90
    }
  ]
}
```

**Error 400 (invalid month format)**
```json
{
  "timestamp": "2025-09-11T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    "getMonthlyRewards.month: must match "^\\d{4}-\\d{2}$""
  ]
}
```

**Error 404 (no data)**
```json
{
  "timestamp": "2025-09-11T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found or no transactions",
  "errors": []
}
```

### Get monthly rewards for all customers
**GET** `/api/rewards?month=YYYY-MM`

**Response 200**
```json
[
  {
    "customerId": 42,
    "month": "2025-09",
    "totalRewardPoints": 180,
    "transactions": [
      {
        "transactionId": 10,
        "customerId": 42,
        "amount": 120.0,
        "date": "2025-09-05T14:30:00",
        "rewardPoints": 90
      }
    ]
  },
  {
    "customerId": 77,
    "month": "2025-09",
    "totalRewardPoints": 55,
    "transactions": []
  }
]
```

**Error 400 (invalid month format)**
```json
{
  "timestamp": "2025-09-11T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    "getAllCustomersMonthlyRewards.month: must match "^\\d{4}-\\d{2}$""
  ]
}
```
