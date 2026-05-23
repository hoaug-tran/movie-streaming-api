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

  @NotBlank(message = "Vui lòng nhập mật khẩu mới.")
  @Size(min = 6, max = 255, message = "Mật khẩu phải từ 6 đến 255 ký tự.")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Mật khẩu phải có chữ hoa, chữ thường, số và ký tự đặc biệt.")
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
