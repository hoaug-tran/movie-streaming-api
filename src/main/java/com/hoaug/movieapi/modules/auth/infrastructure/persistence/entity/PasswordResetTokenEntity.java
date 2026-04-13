package com.hoaug.movieapi.modules.auth.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.hoaug.movieapi.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetTokenEntity extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false, unique = true, length = 255)
  private String token;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(name = "used_at")
  private LocalDateTime usedAt;

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

  public LocalDateTime getUsedAt () {
    return usedAt;
  }

  public void setUsedAt (LocalDateTime usedAt) {
    this.usedAt = usedAt;
  }
}
