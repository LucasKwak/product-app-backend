package com.lucaskwak.product_app_backend.security.service;

import com.lucaskwak.product_app_backend.security.dto.in.AuthenticationRequest;
import com.lucaskwak.product_app_backend.security.dto.in.UserDto;
import com.lucaskwak.product_app_backend.security.dto.out.AuthenticationResponse;
import com.lucaskwak.product_app_backend.security.dto.out.ProfileUser;
import com.lucaskwak.product_app_backend.security.dto.out.RegisteredUser;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    RegisteredUser registerOneCustomer(UserDto saveUser);

    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

    ProfileUser findLoggedInUser();

    void logout(HttpServletRequest request);
}
