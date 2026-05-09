package com.hoaug.movieapi.modules.auth.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StartChangePasswordRequest {
  @NotBlank(message = "Mật khẩu hiện tại không được để trống")
  @Size(min = 6, max = 255, message = "Mật khẩu phải từ 6 đến 255 ký tự")
  @ValidSafeString(minLength = 6, maxLength = 255)
  private String currentPassword;

  public String getCurrentPassword () {
    return currentPassword;
  }

  public void setCurrentPassword (String currentPassword) {
    this.currentPassword = currentPassword;
  }
}
