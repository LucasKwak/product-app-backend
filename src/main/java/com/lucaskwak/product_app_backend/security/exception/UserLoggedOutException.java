package com.lucaskwak.product_app_backend.security.exception;

// Esta excepcion puede saltar cuando estamos dentro del JwtAuthenticationFilter
public class UserLoggedOutException extends RuntimeException {
    public UserLoggedOutException() {
    }

    public UserLoggedOutException(String message) {
        super(message);
    }

    public UserLoggedOutException(String message, Throwable cause) {
        super(message, cause);
    }
}
