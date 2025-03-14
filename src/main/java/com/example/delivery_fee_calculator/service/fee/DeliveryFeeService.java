package com.example.delivery_fee_calculator.service.fee;

import org.springframework.stereotype.Service;

/**
 * Service for calculating the total delivery fee based on business rules.
 */
@Service
public class DeliveryFeeService {

    /**
     * Calculates the total delivery fee based on city, vehicle, and weather conditions.
     * Returns null if vehicle usage is forbidden or if given inputs are invalid.
     */
    public Double deliveryFeeCalculator(String city, String vehicle, Double air_temprature, Double wind_speed, String weather_phenomenon){
        // Ensure no null values are given
        if (city == null || vehicle == null || air_temprature == null || wind_speed == null || weather_phenomenon == null) return null;

        // Cleaning and formating of input  data
        city = city.toLowerCase().trim();
        vehicle = vehicle.toLowerCase().trim();
        weather_phenomenon = weather_phenomenon.toLowerCase().trim();

        // Fee calculation
        Double base_fee = baseFeeCalculator(city, vehicle);
        Double extra_fee = extraFeeCalculator(vehicle,air_temprature,wind_speed,weather_phenomenon);

        // Ensure vehicle usage is not forbidden
        return (base_fee != null && extra_fee != null) ? base_fee + extra_fee : null;
    }

    /**
     * Determines the base delivery fee based on city and vehicle type.
     * Returns null if the vehicle type is not from the options (car, scooter, bike)
     */
    private static Double baseFeeCalculator(String city, String vehicle){
        // Utilizes an enhanced switch expression for cleaner and more concise logic
        return switch (city) {
            case "tallinn" -> switch (vehicle) {
                case "car" -> 4.0;
                case "scooter" -> 3.5;
                case "bike" -> 3.0;
                default -> null;
            };
            case "tartu" -> switch (vehicle) {
                case "car" -> 3.5;
                case "scooter" -> 3.0;
                case "bike" -> 2.5;
                default -> null;
            };
            case "pärnu" -> switch (vehicle) {
                case "car" -> 3.0;
                case "scooter" -> 2.5;
                case "bike" -> 2.0;
                default -> null;
            };
            default -> null;
        };
    }

    /**
     * Determines the extra delivery fee based on weather conditions.
     * Returns null if the vehicle is forbidden due to severe weather.
     */
    private static Double extraFeeCalculator(String vehicle, Double air_temprature, Double wind_speed, String weather_phenomenon){
        double feeATEF = 0.0; // Air Temperature Extra Fee
        double feeWSEF = 0.0; // Wind Speed Extra Fee
        double feeWPEF = 0.0; // Weather Phenomenon Extra Fee

        // Extra fee for air temperature (ATEF)
        if (vehicle.equals("scooter") || vehicle.equals("bike")) {
            if (air_temprature < -10.0) {
                feeATEF = 1.0;
            } else if (air_temprature >= -10.0 && air_temprature <= 0.0) {
                feeATEF = 0.5;
            }
        }

        // Extra fee for wind speed (WSEF)
        if (vehicle.equals("bike")) {
            if (wind_speed > 20) {
                return null; // Usage forbidden
            } else if (wind_speed >= 10) {
                feeWSEF = 0.5;
            }
        }

        // Extra fee for weather phenomenon (WPEF)
        if (vehicle.equals("bike") || vehicle.equals("scooter")) {
            if (weather_phenomenon.contains("snow") || weather_phenomenon.contains("sleet")) {
                feeWPEF = 1.0;
            } else if (weather_phenomenon.contains("rain")) {
                feeWPEF = 0.5;
            }
            // If the phenomenon is in the forbidden list, return null immediately
            else if (weather_phenomenon.contains("glaze") || weather_phenomenon.contains("hail") || weather_phenomenon.contains("thunder")) {
                return null; // Usage forbidden
            }
        }

        // If the vehicle is forbidden due to weather, return null
        return feeATEF + feeWSEF + feeWPEF;
    }
}
