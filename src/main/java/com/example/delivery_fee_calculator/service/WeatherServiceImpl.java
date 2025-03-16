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

    /**
     * Constructs a WeatherServiceImpl with the given WeatherRepository.
     * <p>
     *     Spring Boot will automatically inject the WeatherRepository bean via constructor injection.
     * </p>
     *
     * @param weatherRepository the WeatherRepository bean injected by Spring
     */
    public WeatherServiceImpl(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    /**
     * Saves weather information into database
     *
     * @param weather weather information object for saving
     */
    @Override
    public void saveWeather(Weather weather) {
        weatherRepository.save(weather);
    }

    /**
     * Gets all weather information of specific station, ordered by timestamp descending order
     *
     * @param station Takes station name as input
     * @return Returns list of Weather entities
     */
    @Override
    public List<Weather> fetchWeatherByStation(String station) {
        return weatherRepository.findByNameOrderByTimestampDesc(station);
    }
}
