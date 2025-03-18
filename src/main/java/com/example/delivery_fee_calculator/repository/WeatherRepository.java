package com.example.delivery_fee_calculator.repository;

import com.example.delivery_fee_calculator.entity.Weather;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface extending CrudRepository, for database operations. Interface currently is meant for "Weather" entity
 */
@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {

    /**
     * Filters by station name and orders by timestamp descending
     *
     * @param name Given station name for filtering
     * @return Returns a list of Weather entities filtered by station name and ordered by timestamp descending
     */
    @Query("SELECT w from Weather w WHERE w.name = :name ORDER BY w.timestamp DESC")
    List<Weather> findByNameOrderByTimestampDesc(@Param("name") String name);

    /**
     * Filters by station name and timestamp
     *
     * @param name Given station name for filtering
     * @param timestamp Given timestamp for filtering
     * @return Returns a Weather entity filtered by station name and timestamp
     */
    @Query("SELECT w from Weather w WHERE w.name = :name and w.timestamp= :timestamp")
    Weather findByNameAndTimestamp(@Param("name") String name, @Param("timestamp") Long timestamp);
}
