package com.lucaskwak.product_app_backend.controller;

import com.lucaskwak.product_app_backend.security.dto.in.UserDto;
import com.lucaskwak.product_app_backend.security.dto.out.RegisteredUser;
import com.lucaskwak.product_app_backend.security.service.AuthenticationService;
import com.lucaskwak.product_app_backend.security.service.util.RegisteredUserWithJwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    public  ResponseEntity<RegisteredUser> registerOne (@RequestBody UserDto saveUser, HttpServletResponse httpServletResponse) {

        RegisteredUserWithJwt registeredUserWithJwt = authenticationService.registerOneCustomer(saveUser);

        String jwt = registeredUserWithJwt.getJwt();
        RegisteredUser registeredUser = registeredUserWithJwt.getRegisteredUser();

        Cookie cookie = new Cookie("JWT_TOKEN", jwt);
        cookie.setHttpOnly(true);  // Previene el acceso desde JavaScript en el navegador
        cookie.setSecure(false);    // Solo se envía a través de conexiones HTTPS
        cookie.setPath("/");       // Hace que la cookie esté disponible en toda la aplicación
        cookie.setMaxAge(24 * 60 * 60);  // Duración de la cookie (1 día en este caso)

        // Agrega la cookie a la respuesta
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
}
