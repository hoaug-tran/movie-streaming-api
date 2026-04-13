package com.hoaug.movieapi.modules.user.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.hoaug.movieapi.common.model.BaseEntity;
import com.hoaug.movieapi.modules.user.domain.model.AccountStatus;
import com.hoaug.movieapi.modules.user.domain.model.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

  @Column(nullable = false, length = 50, unique = true)
  private String username;

  @Column(nullable = false, length = 100, unique = true)
  private String email;

  @Column(nullable = false, length = 255)
  private String password;

  @Column(name = "full_name", nullable = false, length = 100)
  private String fullName;

  @Column(name = "avatar_url", length = 500)
  private String avatarUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Role role;

  @Enumerated(EnumType.STRING)
  @Column(name = "account_status", nullable = false, length = 20)
  private AccountStatus accountStatus;

  @Column(name = "premium_expiry_date")
  private LocalDateTime premiumExpiryDate;

  @Column(name = "last_login_at")
  private LocalDateTime lastLoginAt;

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

  public LocalDateTime getLastLoginAt () {
    return lastLoginAt;
  }

  public void setLastLoginAt (LocalDateTime lastLoginAt) {
    this.lastLoginAt = lastLoginAt;
  }

}
