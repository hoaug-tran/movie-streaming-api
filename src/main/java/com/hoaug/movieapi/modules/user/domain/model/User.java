package com.hoaug.movieapi.modules.user.domain.model;

import java.time.LocalDateTime;

public class User {
  private Long id;
  private String username;
  private String email;
  private String password;
  private String fullName;
  private String avatarUrl;
  private String profilePictureUrl;
  private String oauthId;
  private String oauthProvider;
  private Role role;
  private AccountStatus accountStatus;
  private LocalDateTime premiumExpiryDate;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime lastLoginAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
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

  public String getPassword () {
    return password;
  }

  public void setPassword (String password) {
    this.password = password;
  }

  public String getFullName () {
    return fullName;
  }

  public void setFullName (String fullName) {
    this.fullName = fullName;
  }

  public String getAvatarUrl () {
    return avatarUrl;
  }

  public void setAvatarUrl (String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public String getProfilePictureUrl () {
    return profilePictureUrl;
  }

  public void setProfilePictureUrl (String profilePictureUrl) {
    this.profilePictureUrl = profilePictureUrl;
  }

  public String getOauthId () {
    return oauthId;
  }

  public void setOauthId (String oauthId) {
    this.oauthId = oauthId;
  }

  public String getOauthProvider () {
    return oauthProvider;
  }

  public void setOauthProvider (String oauthProvider) {
    this.oauthProvider = oauthProvider;
  }

  public Role getRole () {
    return role;
  }

  public void setRole (Role role) {
    this.role = role;
  }

  public AccountStatus getAccountStatus () {
    return accountStatus;
  }

  public void setAccountStatus (AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }

  public LocalDateTime getPremiumExpiryDate () {
    return premiumExpiryDate;
  }

  public void setPremiumExpiryDate (LocalDateTime premiumExpiryDate) {
    this.premiumExpiryDate = premiumExpiryDate;
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

  public LocalDateTime getLastLoginAt () {
    return lastLoginAt;
  }

  public void setLastLoginAt (LocalDateTime lastLoginAt) {
    this.lastLoginAt = lastLoginAt;
  }

}
