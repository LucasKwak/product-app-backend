package com.lucaskwak.product_app_backend.controller;

import com.lucaskwak.product_app_backend.security.dto.in.AuthenticationRequest;
import com.lucaskwak.product_app_backend.security.dto.out.AuthenticationResponse;
import com.lucaskwak.product_app_backend.security.dto.out.LogoutResponse;
import com.lucaskwak.product_app_backend.security.dto.out.ProfileUser;
import com.lucaskwak.product_app_backend.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/profile")
    // Deberia devoler un dto, pero no lo cree que me da pereza
    public ResponseEntity<ProfileUser> getProfile() {
        ProfileUser user = authenticationService.findLoggedInUser();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (
            @RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse response = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout (HttpServletRequest request) {
        authenticationService.logout(request);
        return ResponseEntity.ok(new LogoutResponse("Se ha cerrado correctamente su sesion"));
    }
}
