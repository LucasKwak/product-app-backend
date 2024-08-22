package com.lucaskwak.product_app_backend.controller;

import com.lucaskwak.product_app_backend.security.dto.in.AuthenticationRequest;
import com.lucaskwak.product_app_backend.security.dto.out.AuthenticationResponse;
import com.lucaskwak.product_app_backend.security.dto.out.LogoutResponse;
import com.lucaskwak.product_app_backend.security.dto.out.ProfileUser;
import com.lucaskwak.product_app_backend.security.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<Void> authenticate (
            @RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse httpServletResponse) {
        AuthenticationResponse response = authenticationService.login(authenticationRequest);

        String jwt = response.getJwt();

        Cookie cookie = new Cookie("JWT_TOKEN", jwt);
        cookie.setHttpOnly(true);  // Previene el acceso desde JavaScript en el navegador
        cookie.setSecure(false);    // Solo se envía a través de conexiones HTTPS
        cookie.setPath("/");       // Hace que la cookie esté disponible en toda la aplicación
        cookie.setMaxAge(24 * 60 * 60);  // Duración de la cookie (1 día en este caso)

        // Agrega la cookie a la respuesta
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout (HttpServletRequest httpServletRequest,
                                                  HttpServletResponse httpServletResponse) {
        authenticationService.logout(httpServletRequest);

        Cookie cookie = new Cookie("JWT_TOKEN", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok(new LogoutResponse("Se ha cerrado correctamente su sesion"));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validate (HttpServletRequest httpServletRequest) {
        boolean isTokenValid = authenticationService.validateToken(httpServletRequest);
        return ResponseEntity.ok(isTokenValid);
    }
}
