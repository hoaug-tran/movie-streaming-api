package com.hoaug.movieapi.modules.auth.application.dto.oauth;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class GoogleOAuthUserInfo {
  private String sub;
  private String email;
  private String name;
  private String picture;
  private String accessToken;
  private String refreshToken;
  private LocalDateTime tokenExpiry;
  private String idToken;
  private Map<String, Object> profileData = new HashMap<>();

  public String getSub () {
    return sub;
  }

  public void setSub (String sub) {
    this.sub = sub;
  }

  public String getEmail () {
    return email;
  }

  public void setEmail (String email) {
    this.email = email;
  }

  public String getName () {
    return name;
  }

  public void setName (String name) {
    this.name = name;
  }

  public String getPicture () {
    return picture;
  }

  public void setPicture (String picture) {
    this.picture = picture;
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
}