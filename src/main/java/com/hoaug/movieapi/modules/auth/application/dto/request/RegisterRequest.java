package com.hoaug.movieapi.modules.auth.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
  @NotBlank
  @Size(min = 6, max = 50)
  private String username;

  @NotBlank
  @Email
  @Size(max = 100)
  private String email;

  @NotBlank
  @Size(min = 6, max = 255)
  private String password;

  @NotBlank
  @Size(max = 100)
  private String fullName;

  private String avatarUrl;

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

}
