package com.example.delivery_fee_calculator.repository;

import com.example.delivery_fee_calculator.entity.Weather;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Interface extending CrudRepository
public interface WeatherRepository extends CrudRepository<Weather, Long> {

    // Fetch weather data ordered by timestamp descending
    @Query("SELECT w FROM Weather w ORDER BY w.timestamp DESC")
    List<Weather> findAllByOrderByTimestampDesc();
}
