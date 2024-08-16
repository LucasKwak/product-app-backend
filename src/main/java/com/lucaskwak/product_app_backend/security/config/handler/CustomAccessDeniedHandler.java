package com.lucaskwak.product_app_backend.security.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lucaskwak.product_app_backend.dto.out.ApiErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setBackendMessage(accessDeniedException.getLocalizedMessage());
        apiErrorResponse.setMessage("Acceso denegado. No tiene los permisos necesarios para acceder a esta funcion");
        apiErrorResponse.setTimestamp(LocalDateTime.now());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Este string es como si fuese un json
        String apiErrorAsString = objectMapper.writeValueAsString(apiErrorResponse);
        // Escribimos el json en la respuesta
        response.getWriter().write(apiErrorAsString);
    }
}
