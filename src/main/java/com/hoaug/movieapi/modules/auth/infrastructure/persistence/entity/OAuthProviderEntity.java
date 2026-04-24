package com.hoaug.movieapi.modules.auth.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "oauth_providers")
public class OAuthProviderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 50, unique = true)
  private String name;

  @Column(name = "display_name", nullable = false, length = 100)
  private String displayName;

  @Column(name = "client_id", nullable = false, length = 255)
  private String clientId;

  @Column(name = "client_secret", nullable = false, length = 255)
  private String clientSecret;

  @Column(name = "authorization_uri", nullable = false, length = 500)
  private String authorizationUri;

  @Column(name = "token_uri", nullable = false, length = 500)
  private String tokenUri;

  @Column(name = "user_info_uri", nullable = false, length = 500)
  private String userInfoUri;

  @Column(name = "redirect_uri", nullable = false, length = 500)
  private String redirectUri;

  @Column(name = "scopes", length = 500)
  private String scopes;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt = LocalDateTime.now();

  public OAuthProviderEntity() {
  }

  public OAuthProviderEntity(String name, String displayName) {
    this.name = name;
    this.displayName = displayName;
  }

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public String getName () {
    return name;
  }

  public void setName (String name) {
    this.name = name;
  }

  public String getDisplayName () {
    return displayName;
  }

  public void setDisplayName (String displayName) {
    this.displayName = displayName;
  }

  public String getClientId () {
    return clientId;
  }

  public void setClientId (String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret () {
    return clientSecret;
  }

  public void setClientSecret (String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getAuthorizationUri () {
    return authorizationUri;
  }

  public void setAuthorizationUri (String authorizationUri) {
    this.authorizationUri = authorizationUri;
  }

  public String getTokenUri () {
    return tokenUri;
  }

  public void setTokenUri (String tokenUri) {
    this.tokenUri = tokenUri;
  }

  public String getUserInfoUri () {
    return userInfoUri;
  }

  public void setUserInfoUri (String userInfoUri) {
    this.userInfoUri = userInfoUri;
  }

  public String getRedirectUri () {
    return redirectUri;
  }

  public void setRedirectUri (String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getScopes () {
    return scopes;
  }

  public void setScopes (String scopes) {
    this.scopes = scopes;
  }

  public Boolean getIsActive () {
    return isActive;
  }

  public void setIsActive (Boolean isActive) {
    this.isActive = isActive;
  }

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt () {
    return updatedAt;
  }

  public void setUpdatedAt (LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
