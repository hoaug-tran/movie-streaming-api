package com.hoaug.movieapi.modules.auth.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ForgotPasswordRequest {
  @NotBlank(message = "Vui lòng nhập email.")
  @Email(message = "Email không đúng định dạng.")
  @Size(max = 100, message = "Email tối đa 100 ký tự.")
  private String email;

  public String getEmail () {
    return email;
  }

  public void setEmail (String email) {
    this.email = email;
  }
}
