package com.hoaug.movieapi.modules.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class VerifyEmailChangeOtpRequest {
  @NotBlank(message = "Vui lòng nhập mã OTP.")
  @Pattern(regexp = "^[0-9]{6}$", message = "Mã OTP phải gồm 6 chữ số.")
  private String otp;

  public String getOtp () {
    return otp;
  }

  public void setOtp (String otp) {
    this.otp = otp;
  }
}
