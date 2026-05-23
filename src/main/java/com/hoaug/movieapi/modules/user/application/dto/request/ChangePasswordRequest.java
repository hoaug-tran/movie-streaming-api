package com.hoaug.movieapi.modules.user.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {
  @NotBlank(message = "Vui lòng nhập mật khẩu cũ.")
  @Size(min = 6, max = 255, message = "Mật khẩu phải từ 6 đến 255 ký tự.")
  @ValidSafeString(minLength = 6, maxLength = 255)
  private String oldPassword;

  @NotBlank(message = "Vui lòng nhập mật khẩu mới.")
  @Size(min = 6, max = 255, message = "Mật khẩu phải từ 6 đến 255 ký tự.")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Mật khẩu phải có chữ hoa, chữ thường, số và ký tự đặc biệt.")
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
