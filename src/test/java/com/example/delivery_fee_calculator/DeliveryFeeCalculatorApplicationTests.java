package com.example.delivery_fee_calculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Smoke test to ensure that the Spring Boot application context loads successfully.
 *
 * <p>
 *     Validates that all beans are created and the application starts without errors.
 * </p>
 */
@SpringBootTest
class DeliveryFeeCalculatorApplicationTests {
	@Autowired
	private DeliveryFeeCalculatorApplication context;

	@Test
	void contextLoads() {
		// Makes sure the application context exists
		assertNotNull(context);
	}

}
