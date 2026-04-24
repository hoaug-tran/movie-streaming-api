package com.hoaug.movieapi.modules.auth.domain.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class OAuthCredential {
  private Long id;
  private Long userId;
  private String provider;
  private String oauthId;
  private String accessToken;
  private String refreshToken;
  private LocalDateTime tokenExpiry;
  private String idToken;
  private Map<String, Object> profileData;
  private LocalDateTime connectedAt;
  private LocalDateTime lastUsedAt;
  private boolean active;

  public OAuthCredential() {
    this.profileData = new HashMap<>();
    this.active = true;
  }

  public OAuthCredential(Long userId, String provider, String oauthId, String accessToken) {
    this();
    this.userId = userId;
    this.provider = provider;
    this.oauthId = oauthId;
    this.accessToken = accessToken;
    this.connectedAt = LocalDateTime.now();
  }

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

  public String getProvider () {
    return provider;
  }

  public void setProvider (String provider) {
    this.provider = provider;
  }

  public String getOauthId () {
    return oauthId;
  }

  public void setOauthId (String oauthId) {
    this.oauthId = oauthId;
  }

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

  public LocalDateTime getTokenExpiry () {
    return tokenExpiry;
  }

  public void setTokenExpiry (LocalDateTime tokenExpiry) {
    this.tokenExpiry = tokenExpiry;
  }

  public String getIdToken () {
    return idToken;
  }

  public void setIdToken (String idToken) {
    this.idToken = idToken;
  }

  public Map<String, Object> getProfileData () {
    return profileData;
  }

  public void setProfileData (Map<String, Object> profileData) {
    this.profileData = profileData;
  }

  public LocalDateTime getConnectedAt () {
    return connectedAt;
  }

  public void setConnectedAt (LocalDateTime connectedAt) {
    this.connectedAt = connectedAt;
  }

  public LocalDateTime getLastUsedAt () {
    return lastUsedAt;
  }

  public void setLastUsedAt (LocalDateTime lastUsedAt) {
    this.lastUsedAt = lastUsedAt;
  }

  public boolean isActive () {
    return active;
  }

  public void setActive (boolean active) {
    this.active = active;
  }

  public void updateLastUsed () {
    this.lastUsedAt = LocalDateTime.now();
  }

  public boolean isTokenExpired () {
    if (tokenExpiry == null) {
      return false;
    }
    return LocalDateTime.now().isAfter(tokenExpiry);
  }
}
