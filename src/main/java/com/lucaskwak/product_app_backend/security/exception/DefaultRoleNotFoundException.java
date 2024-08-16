package com.lucaskwak.product_app_backend.security.exception;

public class DefaultRoleNotFoundException extends RuntimeException {
    public DefaultRoleNotFoundException() {
    }

    public DefaultRoleNotFoundException(String message) {
        super(message);
    }

    public DefaultRoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
