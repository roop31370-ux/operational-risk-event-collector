package com.internship.tool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔴 Validation errors (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> response = buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed"
        );

        response.put("errors", fieldErrors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 🔴 Resource Not Found (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException ex) {

        return new ResponseEntity<>(
                buildResponse(HttpStatus.NOT_FOUND, ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    // 🔴 Illegal Argument / Bad Request (400)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            IllegalArgumentException ex) {

        return new ResponseEntity<>(
                buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    // 🔴 Unauthorized (401) – useful for JWT errors
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(
            SecurityException ex) {

        return new ResponseEntity<>(
                buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    // 🔴 Generic error (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {

        ex.printStackTrace(); // 🔥 keep for debugging

        return new ResponseEntity<>(
                buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // 🔧 Common response builder
    private Map<String, Object> buildResponse(HttpStatus status, String message) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);

        return response;
    }
}