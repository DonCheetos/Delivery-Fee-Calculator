package com.example.delivery_fee_calculator.service;

import com.example.delivery_fee_calculator.entity.Weather;
import java.util.List;

public interface WeatherService {
    // Save operation
    Weather saveWeather(Weather weather);

    // Read operation
    List<Weather> fetchWeatherList();

    // Update operation
    Weather updateWeather(Weather weather, Long weatherId);

    // Delete operation
    void deleteWeatherById(Long weatherId);
}
