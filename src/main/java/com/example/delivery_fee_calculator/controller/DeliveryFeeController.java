package com.example.delivery_fee_calculator.controller;

import com.example.delivery_fee_calculator.entity.Delivery;
import com.example.delivery_fee_calculator.entity.Weather;
import com.example.delivery_fee_calculator.service.WeatherService;
import com.example.delivery_fee_calculator.service.fee.DeliveryFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Delivery fee calculation REST api
@RestController
public class DeliveryFeeController {
    @Autowired
    private DeliveryFeeService deliveryFeeService;

    @Autowired
    private WeatherService weatherService;

    // POST request for calculating the total delivery fee
    @PostMapping("/delivery/fee")
    public ResponseEntity<?> deliveryFee(@RequestBody Delivery delivery) {
        // Cleaning and formatting of data
        String city = delivery.city().toLowerCase().trim();
        String vehicle = delivery.vehicle().toLowerCase().trim();

        // Mapping of a city to a weather station
        Map<String, String> stationCityRelation = new HashMap<>();
        stationCityRelation.put("tartu", "Tartu-Tõravere");
        stationCityRelation.put("pärnu", "Pärnu");
        stationCityRelation.put("tallinn", "Tallinn-Harku");

        if (!stationCityRelation.containsKey(city)) return ResponseEntity.badRequest().body("error: city not found");

        List<Weather> weatherList = weatherService.fetchWeatherByStation(stationCityRelation.get(city));

        // Make sure the weather information exists
        if (weatherList.isEmpty()) {
            return ResponseEntity.badRequest().body("error: Weather data not available");
        }
        // Retrieve the first available weather record
        Weather weather = weatherList.getFirst();

        Double fee = deliveryFeeService.deliveryFeeCalculator(city,vehicle,weather.getTemp(),weather.getWind(),weather.getPhenomenon());

        if (fee == null) {
            return ResponseEntity.badRequest().body("error: Usage of selected vehicle type is forbidden");
        }
        return ResponseEntity.ok(fee);
    }
}
