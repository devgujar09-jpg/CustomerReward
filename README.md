# Customer Reward API

A simple Spring Boot project to manage customer transactions and calculate rewards.

## 🚀 How to Run
1. Clone the project  
2. Run with:
       bash
   mvn spring-boot:run
   
3. App will start at `http://localhost:8080`

## 📌 API Endpoints
- Get all transactions by customer
  
  GET /transactions/customer/{customerId}
  

- Get transactions by customer and date range
  
  GET /transactions/customer/{customerId}/date-range?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
  

# 🛠 Tech Stack
- Spring Boot
- Spring Data JPA
- H2 Database
- JUnit + Mockito
