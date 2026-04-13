package com.hoaug.movieapi.modules.auth.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ForgotPasswordRequest {
  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  @Size(max = 100, message = "Email must be at most 100 characters")
  private String email;

  public String getEmail () {
    return email;
  }

  public void setEmail (String email) {
    this.email = email;
  }
}
