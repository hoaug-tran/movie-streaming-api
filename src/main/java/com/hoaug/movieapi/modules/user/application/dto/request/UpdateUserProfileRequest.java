package com.hoaug.movieapi.modules.user.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUserProfileRequest {
  @NotBlank(message = "Vui lòng nhập họ tên.")
  @Size(min = 1, max = 100, message = "Họ tên phải từ 1 đến 100 ký tự.")
  @ValidSafeString(minLength = 1, maxLength = 100)
  private String fullName;

  @Size(max = 500, message = "Đường dẫn ảnh đại diện tối đa 500 ký tự.")
  @ValidSafeString(minLength = 0, maxLength = 500)
  private String avatarUrl;

  public String getFullName () {
    return fullName;
  }

  public void setFullName (String fullName) {
    this.fullName = fullName;
  }

  public String getAvatarUrl () {
    return avatarUrl;
  }

  public void setAvatarUrl (String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

}
