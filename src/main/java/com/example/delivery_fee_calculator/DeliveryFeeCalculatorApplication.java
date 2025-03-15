	package com.example.delivery_fee_calculator;

	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.scheduling.annotation.EnableScheduling;

	/**
	 * Main entry point for delivery fee calculator application.
	 * <p> This class starts up Spring Boot application and enables scheduling for periodic tasks</p>
	 */
	@SpringBootApplication
	@EnableScheduling
	public class DeliveryFeeCalculatorApplication {

		public static void main(String[] args) {
			SpringApplication.run(DeliveryFeeCalculatorApplication.class, args);
		}

	}
