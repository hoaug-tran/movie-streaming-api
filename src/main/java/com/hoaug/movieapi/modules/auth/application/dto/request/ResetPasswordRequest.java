package com.hoaug.movieapi.modules.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
  @NotBlank
  private String token;

  @NotBlank
  @Size(min = 6, max = 255)
  private String newPassword;

  public String getToken () {
    return token;
  }

  public void setToken (String token) {
    this.token = token;
  }

  public String getNewPassword () {
    return newPassword;
  }

  public void setNewPassword (String newPassword) {
    this.newPassword = newPassword;
  }
}
