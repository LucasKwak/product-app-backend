package com.lucaskwak.product_app_backend.dto.in;


import java.io.Serializable;

public class CategoryDto implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
