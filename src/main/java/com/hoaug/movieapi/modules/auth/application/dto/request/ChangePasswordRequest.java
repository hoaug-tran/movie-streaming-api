package com.hoaug.movieapi.modules.auth.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {
  @NotBlank(message = "Old password is required")
  @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
  @ValidSafeString(minLength = 6, maxLength = 255)
  private String oldPassword;

  @NotBlank(message = "New password is required")
  @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain uppercase, lowercase, number and special character")
  @ValidSafeString(minLength = 6, maxLength = 255)
  private String newPassword;

  public String getOldPassword () {
    return oldPassword;
  }

  public void setOldPassword (String oldPassword) {
    this.oldPassword = oldPassword;
  }

  public String getNewPassword () {
    return newPassword;
  }

  public void setNewPassword (String newPassword) {
    this.newPassword = newPassword;
  }
}
