# Customer Reward System

A Spring Boot application to track customer transactions and calculate reward points based on purchase amounts.  

---

## ✅ Project Standards

1. **Unwanted Files**  
   - `.git`, `.classpath`, `.project`, `target/` removed.  
   - `.gitignore` updated to exclude:  
     ```
     mvn/
     mvnw
     mvnw.cmd
     .classpath
     .project
     target/
     ```

2. **Lombok for Entities & DTOs**  
   - No manual getters/setters.  
   - All DTOs and Entities use Lombok annotations like `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`.

3. **Service Layer Naming Conventions**  
   - Repository variables follow lowerCamelCase.  
   - Example:  
     ```java
     private final TransactionRepository repository;
     ```

4. **Test Class Structure**  
   - Exactly one test class per production class.  
   - Example:  
     - `RewardServiceImpl` → `RewardServiceImplTest`  
     - `TransactionServiceImpl` → `TransactionServiceImplTest`  
     - `RewardController` → `RewardControllerTest`  
     - `TransactionController` → `TransactionControllerTest`

---

## 📂 Project Structure
```
src/
 ├── main/java/com/api/customer
 │   ├── controller/         # REST Controllers
 │   ├── dto/                # DTOs with Lombok
 │   ├── exception/          # Custom exceptions
 │   ├── model/              # Entities with Lombok
 │   ├── repository/         # Spring Data Repositories
 │   ├── service/            # Service interfaces
 │   └── serviceImpl/        # Service implementations
 └── test/java/com/api/customer
     ├── controller/         # Controller tests
     └── serviceImpl/        # Service tests
```

---

## 🧪 Testing
- Run tests:
  ```bash
  ./mvnw clean test
  ```
- Coverage:
  - Controllers tested with mocked services.  
  - Services tested with mocked repositories.  
  - Edge cases (invalid inputs, exceptions) included.

---

## 🚀 Running the Application
1. Build:
   ```bash
   ./mvnw clean install
   ```
2. Run:
   ```bash
   ./mvnw spring-boot:run
   ```
3. API Endpoints:  
   - `GET /api/customers/{id}/rewards`  
   - `GET /api/customers/{id}/rewards?month=yyyy-MM`  
   - `GET /api/rewards?month=yyyy-MM`  
   - `GET /api/customers/{id}/transactions`  
   - `GET /api/customers/{id}/transactions?startDate&endDate`  
   - `POST /api/transactions`


---

## 📖 Examples

### Create Transaction
**Request**
```http
POST /api/transactions
Content-Type: application/json

{
  "customerId": "1",
  "amount": 120.0,
  "transactionDate": "2025-09-10T12:00:00Z"
}
```

**Response**
```json
{
  "id": 1,
  "customerId": "1",
  "amount": 120.0,
  "transactionDate": "2025-09-10T12:00:00Z"
}
```

### Get All-Time Rewards
**Request**
```http
GET /api/customers/1/rewards
```

**Response**
```json
{
  "customerId": 1,
  "totalPoints": 90,
  "period": "ALL_TIME"
}
```

### Get Monthly Rewards
**Request**
```http
GET /api/customers/1/rewards?month=2025-09
```

**Response**
```json
{
  "customerId": 1,
  "totalPoints": 40,
  "period": "2025-09"
}
```


### Get Transactions by Date Range
**Request**
```http
GET /api/customers/1/transactions?startDate=2025-09-01&endDate=2025-09-30
```

**Response**
```json
[
  {
    "id": 1,
    "customerId": "1",
    "amount": 75.0,
    "transactionDate": "2025-09-05T14:30:00Z"
  },
  {
    "id": 2,
    "customerId": "1",
    "amount": 45.0,
    "transactionDate": "2025-09-15T10:15:00Z"
  }
]
```


### Error Response Example
**Request**
```http
GET /api/customers/999/rewards
```

**Response**
```json
{
  "timestamp": "2025-09-17T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with ID: 999",
  "path": "/api/customers/999/rewards"
}
```


### Error Response Example (Invalid Date Range)
**Request**
```http
GET /api/customers/1/transactions?startDate=2025-09-30&endDate=2025-09-01
```

**Response**
```json
{
  "timestamp": "2025-09-17T12:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Start date must be before or equal to end date",
  "path": "/api/customers/1/transactions"
}
```
