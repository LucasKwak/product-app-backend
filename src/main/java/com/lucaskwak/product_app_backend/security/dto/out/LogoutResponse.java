package com.lucaskwak.product_app_backend.security.dto.out;

import java.io.Serializable;

// Se devuelve cuando un usuario cierra sesion
public class LogoutResponse implements Serializable {
    private String message;

    public LogoutResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
