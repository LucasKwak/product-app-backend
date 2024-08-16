package com.lucaskwak.product_app_backend.security.dto.out;

import java.io.Serializable;

// Se devuelve cuando se autentica un usuario
public class AuthenticationResponse implements Serializable {

    private String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
