package com.example.delivery_fee_calculator.controller;

import com.example.delivery_fee_calculator.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliveryFeeController {
    @Autowired
    private WeatherService weatherService;


}
