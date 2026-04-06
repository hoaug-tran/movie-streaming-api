package com.hoaug.movieapi.modules.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
  @NotBlank
  private String usernameOrEmail;

  @NotBlank
  @Size(min = 6, max = 255)
  private String password;

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

}
