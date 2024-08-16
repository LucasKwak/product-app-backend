package com.lucaskwak.product_app_backend.security.service.impl;

import com.lucaskwak.product_app_backend.security.persistence.entity.JwtToken;
import com.lucaskwak.product_app_backend.security.persistence.repository.JwtTokenRepository;
import com.lucaskwak.product_app_backend.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtTokenRepository jwtTokenRepository;

    @Value("${security.jwt.expiration-in-minutes}")
    private long EXPIRATION_TIME_IN_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    private SecretKey generateKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Autowired
    public JwtServiceImpl(JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenRepository = jwtTokenRepository;
    }

    @Override
    public String generateToken(UserDetails user, Map<String, Object> extraClaims) {

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + EXPIRATION_TIME_IN_MINUTES * 60 * 1000);

        /*
        return Jwts.builder()
                // Informacion del Payload
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                // Informacion del Header
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                // Para firmar el token
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                // Obtner el String
                .compact();*/
        return Jwts.builder()
                // Informacion del Header
                .header()
                    .type("JWT")
                .and()
                // Informacion del Payload
                .subject(user.getUsername())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .claims(extraClaims) // Estos son claims que pusimos a mayores: NAME, ROLE y AUTHORITIES
                // Para firmar el token
                .signWith(generateKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    private Claims extractAllClaims(String jwt) {

        /*
        return Jwts.parserBuilder()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(jwt) //Jws: jwt signed
                .getBody();*/

        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }


    @Override
    @Transactional
    public void createToken(JwtToken jwtToken) {
        jwtTokenRepository.save(jwtToken);
    }

    @Override
    public Optional<JwtToken> findTokenByToken(String token) {
        return jwtTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void updateToken(JwtToken jwtToken) {
        jwtTokenRepository.save(jwtToken);
    }
}
