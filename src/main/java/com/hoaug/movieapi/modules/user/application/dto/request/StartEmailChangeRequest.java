package com.hoaug.movieapi.modules.user.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StartEmailChangeRequest {
  @NotBlank(message = "Vui lòng nhập email mới.")
  @Email(message = "Email không đúng định dạng.")
  @Size(max = 100, message = "Email tối đa 100 ký tự.")
  @ValidSafeString(minLength = 3, maxLength = 100)
  private String newEmail;

  public String getNewEmail () {
    return newEmail;
  }

  public void setNewEmail (String newEmail) {
    this.newEmail = newEmail;
  }
}
