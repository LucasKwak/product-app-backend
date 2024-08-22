package com.lucaskwak.product_app_backend.security.dto.out;

import com.lucaskwak.product_app_backend.security.oauth2.AuthProvider;

import java.io.Serializable;

// Se devuelve cuando un usuario se registra
public class RegisteredUser implements Serializable {

    private Long id;

    private String username;

    private String email;

    private String name;

    private String role;

    private AuthProvider authProvider;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }
}
