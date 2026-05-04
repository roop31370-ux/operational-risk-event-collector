package com.internship.tool.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 🔴 Validation errors (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        log.warn("Validation failed at {}: {}", request.getRequestURI(), fieldErrors);

        Map<String, Object> response = buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                request.getRequestURI()
        );

        response.put("errors", fieldErrors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 🔴 Resource Not Found (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        log.warn("Resource not found at {}: {}", request.getRequestURI(), ex.getMessage());

        return new ResponseEntity<>(
                buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()),
                HttpStatus.NOT_FOUND
        );
    }

    // 🔴 Bad Request (400)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.warn("Bad request at {}: {}", request.getRequestURI(), ex.getMessage());

        return new ResponseEntity<>(
                buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI()),
                HttpStatus.BAD_REQUEST
        );
    }

    // 🔴 Unauthorized (401) — Authentication failure
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(
            AuthenticationException ex,
            HttpServletRequest request) {

        log.warn("Unauthorized access at {}: {}", request.getRequestURI(), ex.getMessage());

        return new ResponseEntity<>(
                buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", request.getRequestURI()),
                HttpStatus.UNAUTHORIZED
        );
    }

    // 🔴 Forbidden (403) — Access denied (RBAC)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleForbidden(
            AccessDeniedException ex,
            HttpServletRequest request) {

        log.warn("Access denied at {}: {}", request.getRequestURI(), ex.getMessage());

        return new ResponseEntity<>(
                buildResponse(HttpStatus.FORBIDDEN, "Access denied", request.getRequestURI()),
                HttpStatus.FORBIDDEN
        );
    }

    // 🔴 Generic error (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        log.error("Internal error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Something went wrong",
                        request.getRequestURI()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // 🔧 Common response builder
    private Map<String, Object> buildResponse(HttpStatus status, String message, String path) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        response.put("path", path);

        return response;
    }
}