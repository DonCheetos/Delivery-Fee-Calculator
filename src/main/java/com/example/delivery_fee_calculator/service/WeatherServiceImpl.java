package com.example.delivery_fee_calculator.service;

import com.example.delivery_fee_calculator.entity.Weather;
import com.example.delivery_fee_calculator.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
// Class
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    // Save weather information into db
    @Override
    public Weather saveWeather(Weather weather) {
        return weatherRepository.save(weather);
    }

    // Get all weather information from db
    @Override
    public List<Weather> fetchWeatherList() {
        return (List<Weather>) weatherRepository.findAll();
    }

    // Update weather information by id
    @Override
    public Weather updateWeather(Weather weather, Long weatherId) {
        Weather wetDB;

        // Try to find the weather from database
        try {
            wetDB = weatherRepository.findById(weatherId).get();
        } catch (Exception e) {
            throw new RuntimeException();
        }
        // Update station name
        if (Objects.nonNull(weather.getName()) && !"".equalsIgnoreCase(weather.getName())) {
            wetDB.setName(weather.getName());
        }

        // Update station WMO
        if (Objects.nonNull(weather.getWmo()) && !"".equalsIgnoreCase(weather.getWmo())) {
            wetDB.setWmo(weather.getWmo());
        }

        // Update weather Temp
        if (Objects.nonNull(weather.getTemp())) {
            wetDB.setTemp(weather.getTemp());
        }

        // Update weather Wind
        if (Objects.nonNull(weather.getWind())) {
            wetDB.setWind(weather.getWind());
        }

        // Update weather Phenomenon
        if (Objects.nonNull(weather.getPhenomenon()) && !"".equalsIgnoreCase(weather.getPhenomenon())) {
            wetDB.setPhenomenon(weather.getPhenomenon());
        }

        // Update observation timestamp
        if (Objects.nonNull(weather.getTimestamp()) && !"".equalsIgnoreCase(weather.getTimestamp())) {
            wetDB.setTimestamp(weather.getTimestamp());
        }

        return weatherRepository.save(wetDB);
    }

    // Delete weather information by id
    @Override
    public void deleteWeatherById(Long weatherId) {
        weatherRepository.deleteById(weatherId);
    }
}
