package com.example.delivery_fee_calculator.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global REST exception handler that manages error responses.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Handles malformed JSON requests or missing/wrong fields.
     *
     * @return Returns HTTP 400 (bad request) with an error message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidJson(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid request body: please provide a valid JSON");
        return ResponseEntity.badRequest().body(error);
    }
}
