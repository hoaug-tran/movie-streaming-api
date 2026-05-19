package com.hoaug.movieapi.modules.streaming.application.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class OfflineTokenService {

  private static final long OFFLINE_TOKEN_TTL_MS = 48L * 60 * 60 * 1000;

  private final SecretKey signingKey;

  public OfflineTokenService (
      @Value("${jwt.secret-key}") String jwtSecret) {
    this.signingKey = Keys.hmacShaKeyFor(jwtSecret.trim().getBytes(StandardCharsets.UTF_8));
  }

  public String generateOfflineToken (Long userId, Long episodeId, String quality) {
    Date now = new Date();
    Date expiresAt = new Date(now.getTime() + OFFLINE_TOKEN_TTL_MS);
    return Jwts.builder()
        .subject(String.valueOf(userId))
        .claim("episodeId", episodeId)
        .claim("quality", quality)
        .claim("type", "offline")
        .issuedAt(now)
        .expiration(expiresAt)
        .signWith(signingKey)
        .compact();
  }

  public Claims validateOfflineToken (String token) {
    try {
      return Jwts.parser()
          .verifyWith(signingKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (JwtException e) {
      return null;
    }
  }

  public boolean isValidOfflineToken (String token, Long episodeId, String quality) {
    Claims claims = validateOfflineToken(token);
    if (claims == null) return false;
    Long claimEpisodeId = claims.get("episodeId", Long.class);
    String claimQuality = claims.get("quality", String.class);
    String claimType = claims.get("type", String.class);
    return "offline".equals(claimType)
        && episodeId.equals(claimEpisodeId)
        && quality.equals(claimQuality);
  }

  public long getOfflineTtlMs () {
    return OFFLINE_TOKEN_TTL_MS;
  }
}
