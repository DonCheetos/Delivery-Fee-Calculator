package com.example.delivery_fee_calculator.controller;

import com.example.delivery_fee_calculator.entity.Delivery;
import com.example.delivery_fee_calculator.entity.Weather;
import com.example.delivery_fee_calculator.service.WeatherService;
import com.example.delivery_fee_calculator.service.fee.DeliveryFeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <!DOCTYPE html>
 * <html>
 *    Delivery fee calculation REST interface.
 *    <p>
 *       This REST controller provides an endpoint to calculate the delivery fee based on the provided city
 *       and vehicle type. The calculation takes into account the latest weather data retrieved from the database.
 *    </p>
 *    <p>
 *       <b>Accepted JSON Input Format (Example):</b>
 *    <pre>
 *   {
 *     "city": "Tallinn",
 *     "vehicle": "Car"
 *   }
 *   </pre>
 *    </p>
 *    <p>
 *       <b>Allowed Values:</b>
 *    <ul>
 *       <li><b>city</b>: "Tallinn", "Tartu", "Pärnu"</li>
 *       <li><b>vehicle</b>: "Car", "Scooter", "Bike"</li>
 *    </ul>
 *    </p>
 *    <p><b>Response:</b>
 *    <ul>
 *       <li>HTTP 200 with the calculated delivery fee {@code {"fee" : value_given}} on success.</li>
 *       <li>
 *          HTTP 400 with an error message on failure:
 *          <ul>
 *             <li>{@code {"error": "City not found"}} - if the provided city is not in the allowed list. List of allowed: "Tallinn", "Tartu", "Pärnu"</li>
 *             <li>{@code {"error" : "Weather data not available"}} - if no weather data is found for the city.</li>
 *             <li>{@code {"error" : "Usage of selected vehicle type is forbidden"}} - if business rules disallow the selected vehicle type.</li>
 *          </ul>
 *       </li>
 *    </ul>
 *    </p>
 * </html>
 */
@RestController
public class DeliveryFeeController {

    private final DeliveryFeeService deliveryFeeService;

    private final WeatherService weatherService;

    // Constructor Injection: Spring automatically injects the required beans
    public DeliveryFeeController(DeliveryFeeService deliveryFeeService, WeatherService weatherService) {
        this.deliveryFeeService = deliveryFeeService;
        this.weatherService = weatherService;
    }

    /**
     * Calculates the total delivery fee based on city, vehicle type, and current weather conditions.
     *
     * @param delivery A JSON object containing "city" and "vehicle" parameters.
     * @return A ResponseEntity containing either the calculated fee (HTTP 200) or an error message (HTTP 400).
     */
    @PostMapping("/delivery/fee")
    public ResponseEntity<?> deliveryFee(@Validated @RequestBody Delivery delivery) {
        // Cleaning and formatting of data
        String city = delivery.city().toLowerCase().trim();
        String vehicle = delivery.vehicle().toLowerCase().trim();

        // Mapping of a city to a weather station
        Map<String, String> stationCityRelation = new HashMap<>();
        stationCityRelation.put("tartu", "Tartu-Tõravere");
        stationCityRelation.put("pärnu", "Pärnu");
        stationCityRelation.put("tallinn", "Tallinn-Harku");

        // Valdiates the city is in the known list
        if (!stationCityRelation.containsKey(city)) return ResponseEntity.badRequest().body(Map.of("error", "City not found"));

        // Get list of weathers by station
        List<Weather> weatherList = weatherService.fetchWeatherByStation(stationCityRelation.get(city));

        // Make sure the weather information exists
        if (weatherList.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Weather data not available"));
        }
        // Retrieve the first available weather record
        Weather weather = weatherList.getFirst();

        // Calculate the delivery fee with provided city, vehicle and weather information
        Double fee = deliveryFeeService.deliveryFeeCalculator(city,vehicle,weather.getTemp(),weather.getWind(),weather.getPhenomenon());

        // If the usage of vehicle type is forbidden
        if (fee == null) {
            return ResponseEntity.badRequest().body(Map.of("error","Usage of selected vehicle type is forbidden"));
        }
        return ResponseEntity.ok(Map.of("fee", fee));
    }
}
