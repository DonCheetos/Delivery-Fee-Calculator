package com.example.delivery_fee_calculator.service;

import com.example.delivery_fee_calculator.entity.Weather;
import java.util.List;

/**
 * Interface for weather data CRUD operations.
 */
public interface WeatherService {
    /**
     * Save operation
     *
     * @param weather weather information object for saving
     */
    void saveWeather(Weather weather);

    /**
     * Read all operation, filtered by station
     *
     * @param station takes station name as input
     * @return returns list of weather information by station name
     */
    List<Weather> fetchWeatherByStation(String station);

    /**
     * Read operation, filtered by station and timestamp
     *
     * @param station takes station name as input
     * @param timestamp takes timestamp as input
     * @return returns weather information by station name and timestamp
     */
    Weather fetchWeatherByStationAndTimestamp(String station, Long timestamp);
}
