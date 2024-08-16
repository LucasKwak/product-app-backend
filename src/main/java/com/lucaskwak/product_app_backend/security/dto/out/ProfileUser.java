package com.lucaskwak.product_app_backend.security.dto.out;

import java.io.Serializable;

// Se devuelve cuando un usuario solicita la informacion de su cuenta
public class ProfileUser implements Serializable {

    private long id;

    private String name;

    private String username;

    private String role;

    private String operations;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOperations() {
        return operations;
    }

    public void setOperations(String operations) {
        this.operations = operations;
    }
}
