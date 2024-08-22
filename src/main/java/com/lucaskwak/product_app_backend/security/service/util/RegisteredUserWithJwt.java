package com.lucaskwak.product_app_backend.security.service.util;

import com.lucaskwak.product_app_backend.security.dto.out.RegisteredUser;

public class RegisteredUserWithJwt {
    private RegisteredUser registeredUser;
    private String jwt;

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
