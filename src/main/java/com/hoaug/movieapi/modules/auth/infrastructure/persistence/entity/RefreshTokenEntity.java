package com.hoaug.movieapi.modules.auth.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.hoaug.movieapi.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false, unique = true, length = 255)
  private String token;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(name = "revoked_at")
  private LocalDateTime revokedAt;

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
}
