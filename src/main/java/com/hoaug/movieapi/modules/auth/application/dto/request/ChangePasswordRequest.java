package com.hoaug.movieapi.modules.auth.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {
  @NotBlank(message = "Mật khẩu hiện tại không được để trống")
  @Size(min = 6, max = 255, message = "Mật khẩu phải từ 6 đến 255 ký tự")
  @ValidSafeString(minLength = 6, maxLength = 255)
  private String currentPassword;

  @NotBlank(message = "Mật khẩu mới không được để trống")
  @Size(min = 6, max = 255, message = "Mật khẩu phải từ 6 đến 255 ký tự")
  @ValidSafeString(minLength = 6, maxLength = 255)
  private String newPassword;

  @NotBlank(message = "Challenge token không được để trống")
  private String challengeToken;

  @NotBlank(message = "Mã OTP không được để trống")
  @Pattern(regexp = "^\\d{6}$", message = "Mã OTP phải gồm 6 chữ số")
  private String otp;

  public String getCurrentPassword () {
    return currentPassword;
  }

  public void setCurrentPassword (String currentPassword) {
    this.currentPassword = currentPassword;
  }

  public String getNewPassword () {
    return newPassword;
  }

  public void setNewPassword (String newPassword) {
    this.newPassword = newPassword;
  }

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
}

