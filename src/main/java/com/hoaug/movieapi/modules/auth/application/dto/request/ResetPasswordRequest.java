package com.hoaug.movieapi.modules.auth.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
  @NotBlank(message = "Reset token is required")
  @Size(min = 1, max = 1024, message = "Token must be between 1 and 1024 characters")
  @ValidSafeString(minLength = 1, maxLength = 1024)
  private String token;

  @NotBlank(message = "New password is required")
  @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain uppercase, lowercase, number and special character")
  @ValidSafeString(minLength = 6, maxLength = 255)
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
