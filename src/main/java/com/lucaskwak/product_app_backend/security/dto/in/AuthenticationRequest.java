package com.lucaskwak.product_app_backend.security.dto.in;

import java.io.Serializable;

// Se entrega cuando un usuario quiere iniciar sesion
public class AuthenticationRequest implements Serializable {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
