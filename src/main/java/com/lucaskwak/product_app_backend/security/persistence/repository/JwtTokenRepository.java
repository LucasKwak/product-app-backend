package com.lucaskwak.product_app_backend.security.persistence.repository;

import com.lucaskwak.product_app_backend.security.persistence.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByToken(String token);
}
