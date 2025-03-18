# Delivery-Fee-Calculator

## Overview

A REST API that calculates delivery fees based on:
- City (Tallinn, Tartu, Pärnu)
- Vehicle type (Car, Scooter, Bike)
- Latest weather data from the Estonian Environment Agency

The fee includes regional base fees and extra charges depending on weather conditions.

## Technologies

- Java 21
- Spring Boot
- H2 Database
- Spring Scheduling (CronJob)
- REST API (Spring Web)
- JUnit 5, Mockito
- Lombok (Boilerplate code reduction)

## Getting Started

Clone the repository and run the project:
```bash
./gradlew bootRun
```

Base URL: [http://localhost:8082/delivery/fee](http://localhost:8082/delivery/fee)

Weather data is automatically imported every hour, 15 minutes after a full hour (`HH:15:00`).

## REST API

### **POST** `/delivery/fee`

### Request (JSON)
```json
{
  "city": "Tartu",
  "vehicle": "Car"
}
```
*(Optional):*
```json
{
  "city": "Tartu",
  "vehicle": "Car",
  "timestamp": 1741972499
}
```

### Successful response (200 OK)
```json
{
  "fee": 3.5
}
```
**Normal flow of events.**

### Error: Usage of selected vehicle type is forbidden (400 Bad Request)
```json
{
  "error": "Usage of selected vehicle type is forbidden"
}
```
**This occurs when the business rules forbid the selected vehicle type or the vehicle type is unknown**

### Error: City not found (400 Bad Request)
```json
{
  "error": "City not found"
}
```
**This occurs when the given city is unknown.**

### Error: Weather data not available (400 Bad Request)
```json
{
  "error": "Weather data not available"
}
```
**This occurs when there is no weather data in the database to calculate the delivery fee.**

### Error: Invalid JSON provided (400 Bad Request)
```json
{
  "error": "Invalid request body: please provide a valid JSON"
}
```
**This occurs when the provided JSON request is malformed or has missing/wrong fields.**

