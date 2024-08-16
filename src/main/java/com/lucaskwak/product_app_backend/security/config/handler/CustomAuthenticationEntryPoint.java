package com.lucaskwak.product_app_backend.security.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lucaskwak.product_app_backend.dto.out.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setBackendMessage(authException.getLocalizedMessage());
        apiErrorResponse.setMessage("No se encontraron credenciales de autenticacion. Por favor, inicie sesion para acceder a esta funcion");

        apiErrorResponse.setTimestamp(LocalDateTime.now());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String apiErrorAsString = objectMapper.writeValueAsString(apiErrorResponse);
        response.getWriter().write(apiErrorAsString);

    }
}
