package com.lucaskwak.product_app_backend.security.service.impl;

import com.lucaskwak.product_app_backend.security.dto.in.AuthenticationRequest;
import com.lucaskwak.product_app_backend.security.dto.in.UserDto;
import com.lucaskwak.product_app_backend.security.dto.out.AuthenticationResponse;
import com.lucaskwak.product_app_backend.security.dto.out.ProfileUser;
import com.lucaskwak.product_app_backend.security.dto.out.RegisteredUser;
import com.lucaskwak.product_app_backend.security.persistence.entity.JwtToken;
import com.lucaskwak.product_app_backend.security.persistence.entity.Operation;
import com.lucaskwak.product_app_backend.security.persistence.entity.User;
import com.lucaskwak.product_app_backend.security.service.AuthenticationService;
import com.lucaskwak.product_app_backend.security.service.JwtService;
import com.lucaskwak.product_app_backend.security.service.UserService;
import com.lucaskwak.product_app_backend.security.service.util.RegisteredUserWithJwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServiceImpl(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("role", user.getRole().getName());
        extraClaims.put("authorities", user.getAuthorities());

        return extraClaims;
    }

    @Override
    public RegisteredUserWithJwt registerOneCustomer(UserDto saveUser) {

        // Guardamos en la BD al usuario (customer)
        User user = userService.registerOneCustomer(saveUser);

        // Preparamos la respuesta
        RegisteredUser userDtoResponse = new RegisteredUser();
        userDtoResponse.setId(user.getId());
        userDtoResponse.setName(user.getName());
        userDtoResponse.setUsername(user.getUsername());
        userDtoResponse.setEmail(user.getEmail());
        userDtoResponse.setAuthProvider(user.getAuthProvider());
        userDtoResponse.setRole(user.getRole().getName());

        // Hay que crear el JWT
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        saveJwt(user, jwt);

        RegisteredUserWithJwt registeredUserWithJwt = new RegisteredUserWithJwt();
        registeredUserWithJwt.setRegisteredUser(userDtoResponse);
        registeredUserWithJwt.setJwt(jwt);

        return registeredUserWithJwt;
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {

        // Tenemos que crear un Authentication con sus elementos: Principal y Credentials
        // Existen muchas formas de crearlo, optamos por una que estable el Principal como el
        // username y el Credentials como la password
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );

        // Autenticamos nuestro usuario, puede lanzar una AuthenticationException
        // El authenticationManager buscara un proveedor y encontrara el DaoAuthenticationProvider que
        // establecimos en SecurityBeanInjector
        authenticationManager.authenticate(authentication);

        // Si no lanza la excepcion, quiere decir que el usuario es valido, por tanto podemos devolver
        // el JWT (lo creamos ahora)
        // Para ello necesitamos determinar el header y el payload como cuando creamos un Customer

        // Recogemos al usuario de la BD (como antes no hubo ninguna excepcion, el get nunca devuelve null)
        User user = userService.findOneUserByUsername(authenticationRequest.getUsername()).get();

        // Llamamos al servicio para crear el jwt
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        saveJwt(user, jwt);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setJwt(jwt);

        return authenticationResponse;
    }

    private void saveJwt(User user, String jwt) {
        JwtToken tokenToSave = new JwtToken();
        tokenToSave.setToken(jwt);
        tokenToSave.setUser(user);
        tokenToSave.setValid(true);

        jwtService.createToken(tokenToSave);
    }

    @Override
    public ProfileUser findLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        ProfileUser profileUser = new ProfileUser();
        User user = userService.findOneUserByUsername(username).get(); // Nunca va a ser null

        profileUser.setId(user.getId());
        profileUser.setName(user.getName());
        profileUser.setUsername(user.getUsername());
        profileUser.setRole(user.getRole().getName());

        String operationsString = user.getRole().getOperations().stream()
                .map(Operation::getName)
                .collect(Collectors.joining(", "));

        profileUser.setOperations(operationsString);

        return profileUser;
    }

    @Override
    public void logout(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String jwt = "some_token";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Buscar la cookie que te interesa por su nombre
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    // Obtener el valor de la cookie
                    jwt = cookie.getValue();
                }
            }
        }else{
            return;
        }

        Optional<JwtToken> token = jwtService.findTokenByToken(jwt);

        if (token.isPresent() && token.get().isValid()) {
            token.get().setValid(false);
            jwtService.updateToken(token.get());
        }
    }

    @Override
    public Boolean validateToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String jwt = "some_token";
        boolean thereIsJwtTokenInCookie = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Buscar la cookie que te interesa por su nombre
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    // Obtener el valor de la cookie
                    jwt = cookie.getValue();
                    thereIsJwtTokenInCookie = true;
                }
            }
        } else {
            // No se encontraron cookies
            return false;
        }

        if (!thereIsJwtTokenInCookie) {
            // No se encontro la cookie necesaria
            return false;
        }

        try {
            jwtService.extractUsername(jwt);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
