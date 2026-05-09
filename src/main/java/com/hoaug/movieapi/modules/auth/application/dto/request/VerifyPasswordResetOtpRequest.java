package com.hoaug.movieapi.modules.auth.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VerifyPasswordResetOtpRequest extends VerifyOtpRequest {
  @NotBlank(message = "Mật khẩu mới không được để trống")
  @Size(min = 6, max = 255, message = "Mật khẩu phải từ 6 đến 255 ký tự")
  @ValidSafeString(minLength = 6, maxLength = 255)
  private String newPassword;

  public String getNewPassword () {
    return newPassword;
  }

  public void setNewPassword (String newPassword) {
    this.newPassword = newPassword;
  }
}
