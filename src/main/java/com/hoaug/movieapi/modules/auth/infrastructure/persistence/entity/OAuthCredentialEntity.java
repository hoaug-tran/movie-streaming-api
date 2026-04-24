package com.hoaug.movieapi.modules.auth.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.hoaug.movieapi.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "oauth_credentials")
public class OAuthCredentialEntity extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "provider_id", nullable = false)
  private OAuthProviderEntity provider;

  @Column(name = "oauth_id", nullable = false, length = 255)
  private String oauthId;

  @Column(name = "access_token")
  @Lob
  private String accessToken;

  @Column(name = "refresh_token")
  @Lob
  private String refreshToken;

  @Column(name = "token_expiry")
  private LocalDateTime tokenExpiry;

  @Column(name = "id_token")
  @Lob
  private String idToken;

  @Column(name = "profile_data", columnDefinition = "JSON")
  private String profileData;

  @Column(name = "connected_at", nullable = false)
  private LocalDateTime connectedAt;

  @Column(name = "last_used_at")
  private LocalDateTime lastUsedAt;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  public OAuthCredentialEntity() {
  }

  public OAuthCredentialEntity(Long userId, OAuthProviderEntity provider, String oauthId,
      String accessToken) {
    this.userId = userId;
    this.provider = provider;
    this.oauthId = oauthId;
    this.accessToken = accessToken;
    this.connectedAt = LocalDateTime.now();
    this.isActive = true;
  }

  public Long getUserId () {
    return userId;
  }

  public void setUserId (Long userId) {
    this.userId = userId;
  }

  public OAuthProviderEntity getProvider () {
    return provider;
  }

  public void setProvider (OAuthProviderEntity provider) {
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

  public String getProfileData () {
    return profileData;
  }

  public void setProfileData (String profileData) {
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

  public Boolean getIsActive () {
    return isActive;
  }

  public void setIsActive (Boolean isActive) {
    this.isActive = isActive;
  }
}
