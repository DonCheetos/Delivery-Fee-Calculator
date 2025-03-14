package com.example.delivery_fee_calculator.service;

import com.example.delivery_fee_calculator.entity.Weather;
import java.util.List;

// Interface for CRUD operations
public interface WeatherService {
    // Save operation
    Weather saveWeather(Weather weather);

    // Read all operation (Ordered)
    List<Weather> fetchWeatherListOrdered();

    List<Weather> fetchWeatherByStation(String station);

    // Update operation
    Weather updateWeather(Weather weather, Long weatherId);

    // Delete operation
    void deleteWeatherById(Long weatherId);
}
