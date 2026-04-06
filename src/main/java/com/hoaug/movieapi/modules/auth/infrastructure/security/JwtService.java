package com.hoaug.movieapi.modules.auth.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.hoaug.movieapi.config.properties.JwtProperties;
import com.hoaug.movieapi.modules.auth.domain.service.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService implements TokenService {

  private final JwtProperties jwtProperties;
  private final SecureRandom secureRandom = new SecureRandom();

  public JwtService(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
  }

  private SecretKey getSigningKey () {
    return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public String generateAccessToken (String username) {
    Date now = new Date();
    Date expiredAt = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());

    return Jwts.builder().subject(username).issuedAt(now).expiration(expiredAt)
        .signWith(getSigningKey()).compact();
  }

  @Override
  public String generateRefreshToken () {
    byte[] bytes = new byte[64];
    secureRandom.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  @Override
  public String extractUsername (String token) {
    return extractAllClaims(token).getSubject();
  }

  @Override
  public boolean isValidToken (String token, String username) {
    String extractedUsername = extractUsername(token);
    return extractedUsername.equals(username) && !isTokenExpired(token);
  }

  private boolean isTokenExpired (String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
  }

  private Claims extractAllClaims (String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }
}