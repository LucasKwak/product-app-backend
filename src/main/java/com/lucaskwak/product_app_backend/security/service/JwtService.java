package com.lucaskwak.product_app_backend.security.service;

import com.lucaskwak.product_app_backend.security.persistence.entity.JwtToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Optional;

public interface JwtService {

    String generateToken(UserDetails user, Map<String, Object> claims);

    String extractUsername(String jwt);

    String extractRole(String jwt);

    void createToken(JwtToken jwtToken);

    Optional<JwtToken> findTokenByToken(String token);

    void updateToken(JwtToken jwtToken);
}
