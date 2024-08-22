package com.lucaskwak.product_app_backend.security.dto.in;


import com.lucaskwak.product_app_backend.security.oauth2.AuthProvider;

import java.io.Serializable;

// Se entrega cuando un usuario se registra
public class UserDto implements Serializable {

    private String name;

    private String username;

    private String email;

    private String password;

    private AuthProvider authProvider;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }
}
