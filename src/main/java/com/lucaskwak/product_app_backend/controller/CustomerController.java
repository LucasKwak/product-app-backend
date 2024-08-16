package com.lucaskwak.product_app_backend.controller;

import com.lucaskwak.product_app_backend.security.dto.in.UserDto;
import com.lucaskwak.product_app_backend.security.dto.out.RegisteredUser;
import com.lucaskwak.product_app_backend.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final AuthenticationService authenticationService;

    @Autowired
    public CustomerController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public  ResponseEntity<RegisteredUser> registerOne (@RequestBody UserDto saveUser) {
        RegisteredUser registeredUser = authenticationService.registerOneCustomer(saveUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
}
