package com.hoaug.movieapi.modules.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class VerifyEmailChangeOtpRequest {
  @NotBlank(message = "OTP is required")
  @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be 6 digits")
  private String otp;

  public String getOtp () {
    return otp;
  }

  public void setOtp (String otp) {
    this.otp = otp;
  }
}
