package com.example.delivery_fee_calculator.service.fee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests delivery fee calculation service functionality and if business rules were correctly implemented
 */
@SpringBootTest
public class DeliveryFeeServiceTest {

    // Declare the service
    private DeliveryFeeService deliveryFeeService;

    // Initialize the service before each test
    @BeforeEach
    void setUp() {
        deliveryFeeService = new DeliveryFeeService();
    }

    /**
     *  Test base fee calculation for all valid cities and vehicles
     */
    @Test
    void testBaseFeeCalculation(){
        // Assertion for city "Tallinn"
        assertEquals(4.0, deliveryFeeService.deliveryFeeCalculator("Tallinn", "Car", 10.0, 0.0, "Clear"));
        assertEquals(3.5, deliveryFeeService.deliveryFeeCalculator("TaLLinn", "ScOOter", 10.0, 0.0, "Clear"));
        assertEquals(3.0, deliveryFeeService.deliveryFeeCalculator("Tallinn", "BIKE", 10.0, 0.0, "Clear"));

        // Assertion for city "Tartu"
        assertEquals(3.5, deliveryFeeService.deliveryFeeCalculator("Tartu", "Car", 10.0, 0.0, "Clear"));
        assertEquals(3.0, deliveryFeeService.deliveryFeeCalculator("TaRTu", "SCOOTER", 10.0, 0.0, "Clear"));
        assertEquals(2.5, deliveryFeeService.deliveryFeeCalculator("TARTU", "BIKE", 10.0, 0.0, "Clear"));

        // Assertion for city "Pärnu"
        assertEquals(3.0, deliveryFeeService.deliveryFeeCalculator("Pärnu", "Car", 10.0, 0.0, "Clear"));
        assertEquals(2.5, deliveryFeeService.deliveryFeeCalculator("PäRNu", "SCOOTER", 10.0, 0.0, "Clear"));
        assertEquals(2.0, deliveryFeeService.deliveryFeeCalculator("PÄRNU", "BIKE", 10.0, 0.0, "Clear"));

        // Assertion for unknown city
        assertNull(deliveryFeeService.deliveryFeeCalculator("America", "BIKE", 10.0, 0.0, "Clear"));
        assertNull(deliveryFeeService.deliveryFeeCalculator(null, "BIKE", 10.0, 0.0, "Clear"));
        assertNull(deliveryFeeService.deliveryFeeCalculator("", "BIKE", 10.0, 0.0, "Clear"));

        // Assertion for unknown vehicle
        assertNull(deliveryFeeService.deliveryFeeCalculator("Tartu", "hot wheels", 10.0, 0.0, "Clear"));
        assertNull(deliveryFeeService.deliveryFeeCalculator("Tallinn", null, 10.0, 0.0, "Clear"));
        assertNull(deliveryFeeService.deliveryFeeCalculator("Pärnu", "", 10.0, 0.0, "Clear"));
    }

    /**
     *  Tests for extra fee calculation and weather conditions
     */
    @Test
    void testExtraFeeCalculation(){

        // Boundary Value analysis for air temperature (ATEF)
        assertEquals(4.0, deliveryFeeService.deliveryFeeCalculator("Tallinn", "Scooter", -10.0, 0.0, "Clear"));
        assertEquals(3.5, deliveryFeeService.deliveryFeeCalculator("Tallinn", "Bike", 0.0, 0.0, "Clear"));
        assertEquals(3.5, deliveryFeeService.deliveryFeeCalculator("Tallinn", "Scooter", 0.1, 0.0, "Clear"));
        assertEquals(4.0, deliveryFeeService.deliveryFeeCalculator("Tallinn", "Bike", -10.1, 0.0, "Clear"));


        // Boundry Value analysis for wind speed (WSEF)
        assertEquals(3.5, deliveryFeeService.deliveryFeeCalculator("Tallinn", "Bike", 10.0, 10.0, "Clear"));
        assertEquals(3.5, deliveryFeeService.deliveryFeeCalculator("Tallinn", "Bike", 10.0, 20.0, "Clear"));
        assertEquals(3.0, deliveryFeeService.deliveryFeeCalculator("Tallinn", "Bike", 10.0, 9.9, "Clear"));
        assertNull(deliveryFeeService.deliveryFeeCalculator("Tallinn", "Bike", 10.0, 20.1, "Clear"));


        // Assertion for weather phenomenon (WPEF)
        assertEquals(3.0, deliveryFeeService.deliveryFeeCalculator("Tallinn", "BIKE", 10.0, 0.0, "Overcast"));
        assertEquals(3.5, deliveryFeeService.deliveryFeeCalculator("Tallinn", "Scooter", 10.0, 0.0, "Overcast"));

        assertEquals(3.5,deliveryFeeService.deliveryFeeCalculator("Tallinn", "Bike", 10.0, 0.0, "Light rain"));
        assertEquals(4.0,deliveryFeeService.deliveryFeeCalculator("Tallinn", "Scooter", 10.0, 0.0, "Light rain"));

        assertEquals(4.0,deliveryFeeService.deliveryFeeCalculator("Tallinn", "Bike", 10.0, 0.0, "Heavy snow shower"));
        assertEquals(4.5,deliveryFeeService.deliveryFeeCalculator("Tallinn", "Scooter", 10.0, 0.0, "Heavy snow shower"));

        assertEquals(4.0,deliveryFeeService.deliveryFeeCalculator("Tallinn", "Bike", 10.0, 0.0, "Light sleet"));
        assertEquals(4.5,deliveryFeeService.deliveryFeeCalculator("Tallinn", "Scooter", 10.0, 0.0, "Light sleet"));

        assertNull(deliveryFeeService.deliveryFeeCalculator("Tallinn", "BIKE", 10.0, 0.0, "Heavy Clouds with thunder"));
        assertNull(deliveryFeeService.deliveryFeeCalculator("Tallinn", "SCOOTER", 10.0, 0.0, "Heavy Clouds with thunder"));

        assertNull(deliveryFeeService.deliveryFeeCalculator("Tallinn", "BIKE", 10.0, 0.0, "Glaze"));
        assertNull(deliveryFeeService.deliveryFeeCalculator("Tallinn", "SCOOTER", 10.0, 0.0, "Glaze"));

        assertNull(deliveryFeeService.deliveryFeeCalculator("Tallinn", "BIKE", 10.0, 0.0, "Heavy hail"));
        assertNull(deliveryFeeService.deliveryFeeCalculator("Tallinn", "SCOOTER", 10.0, 0.0, "Heavy hail"));

    }

    /**
     *  Tests for null values
     */
    @Test
    void testNullValues() {
        assertNull(deliveryFeeService.deliveryFeeCalculator(null, "Bike", 10.0, 0.0, "Clear"));
        assertNull(deliveryFeeService.deliveryFeeCalculator("Tallinn", null, 10.0, 0.0, "Clear"));
        assertNull(deliveryFeeService.deliveryFeeCalculator("Tallinn", "Bike", null, 0.0, "Clear"));
        assertNull(deliveryFeeService.deliveryFeeCalculator("Tallinn", "Bike", 10.0, null, "Clear"));
        assertNull(deliveryFeeService.deliveryFeeCalculator("Tallinn", "Bike", 10.0, 0.0, null));
    }

    /**
     * Test with combined fees for bike
     */
    @Test
    void testCombinedExtraFeesForBike() {
        assertEquals(4.5, deliveryFeeService.deliveryFeeCalculator("Tallinn", "Bike", -5.0, 15.0, "Moderate rain"));
    }

    /**
     * Test with combined fees for scooter
     */
    @Test
    void testCombinedExtraFeesForScooter() {
        assertEquals(5.0, deliveryFeeService.deliveryFeeCalculator("Tartu", "Scooter", -15.0, 5.0, "Light snowfall"));
    }

    /**
     * Test car under adverse weather conditions
     */
    @Test
    void testCarUnderAdverseWeather() {
        assertEquals(4.0, deliveryFeeService.deliveryFeeCalculator("Tallinn", "Car", -20.0, 25.0, "Hail"));
    }
}
