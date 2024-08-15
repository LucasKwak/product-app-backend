package com.lucaskwak.product_app_backend.dto.inAndOut;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class CategoryDto implements Serializable {

    @NotBlank(message = "El nombre no puede ser vacio")
    private String name;

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }
}
