package com.hoaug.movieapi.modules.auth.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VerifyOtpRequest {
  @NotBlank(message = "Challenge token không hợp lệ")
  @Size(min = 20, max = 255, message = "Challenge token không hợp lệ")
  @ValidSafeString(minLength = 20, maxLength = 255)
  private String challengeToken;

  @NotBlank(message = "Mã OTP không được để trống")
  @Size(min = 6, max = 6, message = "Mã OTP phải gồm đúng 6 chữ số")
  @ValidSafeString(minLength = 6, maxLength = 6)
  private String otp;

  private boolean rememberMe;

  public String getChallengeToken () {
    return challengeToken;
  }

  public void setChallengeToken (String challengeToken) {
    this.challengeToken = challengeToken;
  }

  public String getOtp () {
    return otp;
  }

  public void setOtp (String otp) {
    this.otp = otp;
  }

  public boolean isRememberMe () {
    return rememberMe;
  }

  public void setRememberMe (boolean rememberMe) {
    this.rememberMe = rememberMe;
  }
}
