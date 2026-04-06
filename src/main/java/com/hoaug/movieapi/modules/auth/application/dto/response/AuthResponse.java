package com.hoaug.movieapi.modules.auth.application.dto.response;

import com.hoaug.movieapi.modules.user.domain.model.Role;

public class AuthResponse {
  private String accessToken;
  private String refreshToken;
  private String tokenType;
  private Long userId;
  private String username;
  private String email;
  private String fullName;
  private Role role;

  public String getAccessToken () {
    return accessToken;
  }

  public void setAccessToken (String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken () {
    return refreshToken;
  }

  public void setRefreshToken (String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getTokenType () {
    return tokenType;
  }

  public void setTokenType (String tokenType) {
    this.tokenType = tokenType;
  }

  public Long getUserId () {
    return userId;
  }

  public void setUserId (Long userId) {
    this.userId = userId;
  }

  public String getUsername () {
    return username;
  }

  public void setUsername (String username) {
    this.username = username;
  }

  public String getEmail () {
    return email;
  }

  public void setEmail (String email) {
    this.email = email;
  }

  public String getFullName () {
    return fullName;
  }

  public void setFullName (String fullName) {
    this.fullName = fullName;
  }

  public Role getRole () {
    return role;
  }

  public void setRole (Role role) {
    this.role = role;
  }

}
