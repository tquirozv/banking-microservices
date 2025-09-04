package com.bank.account_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MovementNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMovementNotFound(
            MovementNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        log.debug("Movement not found: {}", ex.getMessage());
        return ResponseEntity.status(status)
            .body(buildErrorBody(status, ex.getMessage(), request.getRequestURI(), null));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAccountNotFound(
            AccountNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        log.debug("Account not found: {}", ex.getMessage());
        return ResponseEntity.status(status)
            .body(buildErrorBody(status, ex.getMessage(), request.getRequestURI(), null));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientFunds(
            InsufficientFundsException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.debug("Insufficient funds: {}", ex.getMessage());
        return ResponseEntity.status(status)
            .body(buildErrorBody(status, ex.getMessage(), request.getRequestURI(), null));
    }

    private Map<String, Object> buildErrorBody(HttpStatus status, String message, String path, Map<String,
            String> validationErrors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);
        if (validationErrors != null && !validationErrors.isEmpty()) {
            body.put("validationErrors", validationErrors);
        }
        return body;
    }
}
