package com.example.delivery_fee_calculator.service;

import com.example.delivery_fee_calculator.entity.Weather;
import com.example.delivery_fee_calculator.repository.WeatherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation that provides operations for weather data.
 * <p>
 *     This service currently supports saving a new weather record and retrieving weather records
 *     for a specific station, ordered by timestamp in descending order.
 * </p>
 *
 */
@Service
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;

    // Constructor Injection: Spring automatically injects the required beans
    public WeatherServiceImpl(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    // Save weather information into db
    @Override
    public void saveWeather(Weather weather) {
        weatherRepository.save(weather);
    }

    // Gets all weather information of specific station, ordered by timestamp descending order
    @Override
    public List<Weather> fetchWeatherByStation(String station) {
        return weatherRepository.findByNameOrderByTimestampDesc(station);
    }
}
