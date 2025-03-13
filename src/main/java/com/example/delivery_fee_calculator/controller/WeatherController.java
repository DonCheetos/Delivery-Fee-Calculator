package com.example.delivery_fee_calculator.controller;

import com.example.delivery_fee_calculator.entity.Weather;
import com.example.delivery_fee_calculator.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// Class
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    // Save operation
    @PostMapping("/weather")
    public Weather saveWeather(@Validated @RequestBody Weather weather){
        return weatherService.saveWeather(weather);
    }

    // Read operation
    @GetMapping("/weather")
    public List<Weather> fetchWeather() {
        return weatherService.fetchWeatherList();
    }

    // Update operation
    @PutMapping("/weather/{id}")
    public Weather updateWeather(@RequestBody Weather weather, @PathVariable("id") Long weatherId) {
        return weatherService.updateWeather(weather, weatherId);
    }

    // Delete operation
    @DeleteMapping("/weather/{id}")
    public String deleteWeatherById(@PathVariable("id") Long weatherId) {
        weatherService.deleteWeatherById(weatherId);
        return "Deleted Successfully";
    }
}