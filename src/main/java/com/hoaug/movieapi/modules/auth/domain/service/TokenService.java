package com.hoaug.movieapi.modules.auth.domain.service;

public interface TokenService {
  String generateAccessToken (String username, String role);

  String generateRefreshToken ();

  String extractUsername (String token);

  String extractRole (String token);

  boolean isValidToken (String token, String username);
}
