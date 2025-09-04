package com.bank.client_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Exception thrown when a requested Client resource cannot be found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ClientNotFoundException(String message) {
        super(message);
    }

}