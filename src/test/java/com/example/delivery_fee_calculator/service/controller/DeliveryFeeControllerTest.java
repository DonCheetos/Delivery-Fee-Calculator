package com.example.delivery_fee_calculator.service.controller;

import com.example.delivery_fee_calculator.entity.Delivery;
import com.example.delivery_fee_calculator.repository.WeatherRepository;
import com.example.delivery_fee_calculator.service.WeatherImportService;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for the DeliveryFeeController REST API.
 * <p>
 *     Validates the full HTTP flow including status codes and JSON response bodies.
 * </p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DeliveryFeeControllerTest {

    // Inject repository to clean the DB during tests
    @Autowired
    WeatherRepository weatherRepository;

    // Inject service to trigger weather data import
    @Autowired
    WeatherImportService weatherImportService;

    // Injects the random port used by Spring Boot during tests
    @LocalServerPort
    private int port;

    // RestTemplate for sending HTTP requests to the application during tests
    TestRestTemplate restTemplate = new TestRestTemplate();

    // Basic HTTP headers
    HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void setUp() {
        // Clear database before usage
        weatherRepository.deleteAll();
        // Trigger weather data import to populate necessary weather info
        weatherImportService.scheduledTrigger();
    }


    /**
     * Tests REST API response when provided with valid city and vehicle type.
     */
    @Test
    public void testDeliveryFeeReturned() throws JSONException {
        Delivery delivery = new Delivery("Tartu", "Car");

        HttpEntity<Delivery> entity = new HttpEntity<>(delivery,headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/delivery/fee"),
                HttpMethod.POST,entity,String.class);

        // Expected JSON output
        String expected = "{\"fee\" : 3.5}";

        System.out.println(expected);

        // Assert HTTP 200 OK status and JSON response match
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), true);
    }

    /**
     * Tests REST API response when provided with an unknown city name
     */
    @Test
    public void testErrorWrongCity() throws JSONException {
        // Prepare an invalid Delivery payload
        Delivery delivery = new Delivery("Narva", "car");

        HttpEntity<Delivery> entity = new HttpEntity<>(delivery,headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/delivery/fee"),
                HttpMethod.POST,entity,String.class);

        // Expected JSON output
        String expected = "{\"error\" : \"City not found\"}";

        System.out.println(expected);

        // Assert HTTP 400 BAD REQUEST status and correct error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), true);
    }

    /**
     * Tests REST API response when missing weather data is missing from the database
     */
    @Test
    public void testErrorNoData() throws JSONException {
        // Clear database again to simulate no weather data situation
        weatherRepository.deleteAll();

        // Prepare a Delivery payload
        Delivery delivery = new Delivery("Tartu", "car");

        HttpEntity<Delivery> entity = new HttpEntity<>(delivery,headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/delivery/fee"),
                HttpMethod.POST,entity,String.class);

        // Expected JSON output
        String expected = "{\"error\" : \"Weather data not available\"}";

        System.out.println(expected);

        // Assert HTTP 400 BAD REQUEST status and correct error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), true);
    }

    /**
     * Tests REST API response when provided with a forbidden or unsupported vehicle type
     */
    @Test
    public void testErrorForbiddenType() throws JSONException {
        // Prepare an invalid Delivery payload
        Delivery delivery = new Delivery("Tartu", "Hot Wheels");

        HttpEntity<Delivery> entity = new HttpEntity<>(delivery,headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/delivery/fee"),
                HttpMethod.POST,entity,String.class);


        String expected = "{\"error\" : \"Usage of selected vehicle type is forbidden\"}";

        System.out.println(expected);

        // Assert HTTP 400 BAD REQUEST status and correct error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), true);
    }

    /**
     * Creates URL with dynamically assigned port and given URI
     *
     * @param uri the URI endpoint (e.g., "/delivery/fee")
     * @return full localhost URL with port
     */
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
