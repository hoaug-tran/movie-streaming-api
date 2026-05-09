package com.hoaug.movieapi.modules.auth.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
  @NotBlank(message = "Username or email is required")
  @Size(min = 1, max = 100, message = "Username or email must be between 1 and 100 characters")
  @ValidSafeString(minLength = 1, maxLength = 100)
  private String usernameOrEmail;

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
  @ValidSafeString(minLength = 6, maxLength = 255)
  private String password;

  private boolean rememberMe;

  public String getUsernameOrEmail () {
    return usernameOrEmail;
  }

  public void setUsernameOrEmail (String usernameOrEmail) {
    this.usernameOrEmail = usernameOrEmail;
  }

  public String getPassword () {
    return password;
  }

  public void setPassword (String password) {
    this.password = password;
  }

  public boolean isRememberMe () {
    return rememberMe;
  }

  public void setRememberMe (boolean rememberMe) {
    this.rememberMe = rememberMe;
  }

}
