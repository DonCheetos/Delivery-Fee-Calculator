package com.example.delivery_fee_calculator.entity;

/**
 * Immutable record representing the request body for a REST delivery fee query.
 * Contains the city and vehicle fields used to calculate the delivery fee.
 */
public record Delivery(String city, String vehicle) {
}
