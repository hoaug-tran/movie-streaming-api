package com.hoaug.movieapi.modules.auth.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RefreshTokenRequest {
  @NotBlank(message = "Refresh token is required")
  @Size(min = 1, max = 1024, message = "Token must be between 1 and 1024 characters")
  @ValidSafeString(minLength = 1, maxLength = 1024)
  private String refreshToken;

  public String getRefreshToken () {
    return refreshToken;
  }

  public void setRefreshToken (String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
