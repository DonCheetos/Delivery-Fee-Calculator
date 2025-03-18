package com.example.delivery_fee_calculator.service;

import com.example.delivery_fee_calculator.entity.Weather;
import com.example.delivery_fee_calculator.repository.WeatherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * Integration tests for WeatherServiceImpl.
 *
 * <p>
 *     Validates saving and fetching weather data between the service layer and database.
 *     Utilizes a temporary test database, not the production one.
 * </p>
 */

@SpringBootTest
@ActiveProfiles("test") // Ensures the tests run against the test DB profile
public class WeatherServiceImplTest {

    @Autowired
    WeatherServiceImpl weatherService;

    @Autowired
    WeatherRepository weatherRepository;

    @BeforeEach
    void setUp() {
        // Clean up before each use
        weatherRepository.deleteAll();
    }

    // Test basic saving operation
    @Test
    public void testWeatherServiceSave() {
        // Test data
        String name = "Test1";
        String wmo = "Test2";
        Double temp = 0.0;
        Double wind = 0.0;
        String phenomenon = "Test3";
        Long timestamp = 123456789L;

        // Create Weather entity
        Weather weather = Weather.builder().name(name).wmo(wmo).temp(temp).wind(wind).phenomenon(phenomenon).timestamp(timestamp).build();

        // Save new test weather information
        weatherService.saveWeather(weather);

        // Asses if the weather information was stored and correctly stored
        Assertions.assertTrue(weatherRepository.findById(weather.getId()).isPresent());
        Weather weatherResult = weatherRepository.findById(weather.getId()).get();
        Assertions.assertEquals(name, weatherResult.getName());
        Assertions.assertEquals(wmo, weatherResult.getWmo());
        Assertions.assertEquals(temp, weatherResult.getTemp());
        Assertions.assertEquals(wind, weatherResult.getWind());
        Assertions.assertEquals(phenomenon, weatherResult.getPhenomenon());
        Assertions.assertEquals(timestamp, weatherResult.getTimestamp());
    }

    // Tests fetching data from database by station name through weather service
    @Test
    public void testFetchWeatherByStation(){
        // Test data
        String name = "Test";
        String wmo = "Test2";
        Double temp = 0.0;
        Double wind = 0.0;
        String phenomenon = "Test3";
        Long timestamp = 123456789L;

        // Create multiple Weather entities
        Weather weather1_1 = Weather.builder().name(name + 1).wmo(wmo).temp(temp).wind(wind).phenomenon(phenomenon).timestamp(timestamp).build();
        Weather weather1_2 = Weather.builder().name(name + 1).wmo(wmo).temp(temp).wind(wind).phenomenon(phenomenon).timestamp(timestamp).build();
        Weather weather1_3 = Weather.builder().name(name + 1).wmo(wmo).temp(temp).wind(wind).phenomenon(phenomenon).timestamp(timestamp).build();
        Weather weather2 = Weather.builder().name(name + 2).wmo(wmo).temp(temp).wind(wind).phenomenon(phenomenon).timestamp(timestamp).build();
        Weather weather3 = Weather.builder().name(name + 3).wmo(wmo).temp(temp).wind(wind).phenomenon(phenomenon).timestamp(timestamp).build();
        Weather weather4 = Weather.builder().name(name + 4).wmo(wmo).temp(temp).wind(wind).phenomenon(phenomenon).timestamp(timestamp).build();

        // Save entities into db
        weatherService.saveWeather(weather1_1);
        weatherService.saveWeather(weather1_2);
        weatherService.saveWeather(weather1_3);
        weatherService.saveWeather(weather2);
        weatherService.saveWeather(weather3);
        weatherService.saveWeather(weather4);

        // Fetch "Test1" type entities from db
        List<Weather> testList = weatherService.fetchWeatherByStation("Test1");

        // Test if there are required amount given and correct data was fetched
        Assertions.assertEquals(3, testList.size());
        for(Weather weather : testList){
            Assertions.assertEquals(name + 1, weather.getName());
        }
    }


    /**
     * Tests fetching data from database by station name and timestamp through weather service
     */
    @Test
    public void testFetchWeatherByStationAndTimestamp(){
        // Test data
        String name = "Test";
        String wmo = "Test2";
        Double temp = 0.0;
        Double wind = 0.0;
        String phenomenon = "Test3";
        Long timestamp = 123455555L;
        Long timestamp2 = 123451111L;

        // Create multiple Weather entities
        Weather weather1 = Weather.builder().name(name).wmo(wmo).temp(temp).wind(wind).phenomenon(phenomenon).timestamp(timestamp).build();
        Weather weather2 = Weather.builder().name(name).wmo(wmo).temp(temp).wind(wind).phenomenon(phenomenon).timestamp(timestamp2).build();
        Weather weather3 = Weather.builder().name(name+1).wmo(wmo).temp(temp).wind(wind).phenomenon(phenomenon).timestamp(timestamp2).build();
        Weather weather4 = Weather.builder().name(name).wmo(wmo).temp(temp).wind(wind).phenomenon(phenomenon).timestamp(timestamp).build();

        // Save entities into db
        weatherService.saveWeather(weather1);
        weatherService.saveWeather(weather2);
        weatherService.saveWeather(weather3);
        weatherService.saveWeather(weather4);

        // Fetch "Test" type entity from db with a timestamp "123451111"
        Weather testRes = weatherService.fetchWeatherByStationAndTimestamp("Test",123451111L);

        // Test if entity exists
        Assertions.assertNotNull(testRes);

        Assertions.assertEquals(123451111L, testRes.getTimestamp());
    }
}
