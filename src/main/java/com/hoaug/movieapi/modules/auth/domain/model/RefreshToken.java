package com.hoaug.movieapi.modules.auth.domain.model;

import java.time.LocalDateTime;

public class RefreshToken {
  private Long id;
  private Long userId;
  private String token;
  private LocalDateTime expiresAt;
  private LocalDateTime revokedAt;
  private LocalDateTime createdAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public Long getUserId () {
    return userId;
  }

  public void setUserId (Long userId) {
    this.userId = userId;
  }

  public String getToken () {
    return token;
  }

  public void setToken (String token) {
    this.token = token;
  }

  public LocalDateTime getExpiresAt () {
    return expiresAt;
  }

  public void setExpiresAt (LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public LocalDateTime getRevokedAt () {
    return revokedAt;
  }

  public void setRevokedAt (LocalDateTime revokedAt) {
    this.revokedAt = revokedAt;
  }

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
