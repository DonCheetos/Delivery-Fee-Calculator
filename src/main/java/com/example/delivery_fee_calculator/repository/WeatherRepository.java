package com.example.delivery_fee_calculator.repository;

import com.example.delivery_fee_calculator.entity.Weather;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
// Interface extending CrudRepository
public interface WeatherRepository extends CrudRepository<Weather, Long>{}
