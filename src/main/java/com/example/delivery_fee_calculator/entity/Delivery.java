package com.example.delivery_fee_calculator.entity;

import lombok.NonNull;

/**
 * Immutable record representing the request body for a REST delivery fee query.
 * Contains the city and vehicle fields used to calculate the delivery fee.
 * <b>(Optional):</b> Can be included a timestamp to get delivery fee for certain period
 */
public record Delivery(@NonNull String city, @NonNull String vehicle, Long timestamp) {
}
