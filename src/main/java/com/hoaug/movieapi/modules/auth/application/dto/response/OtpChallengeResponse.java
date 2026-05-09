package com.hoaug.movieapi.modules.auth.application.dto.response;

public class OtpChallengeResponse {
  private boolean otpRequired;
  private String challengeToken;
  private String email;
  private long expiresInSeconds;
  private long resendAfterSeconds;
  private String message;

  public boolean isOtpRequired () {
    return otpRequired;
  }

  public void setOtpRequired (boolean otpRequired) {
    this.otpRequired = otpRequired;
  }

  public String getChallengeToken () {
    return challengeToken;
  }

  public void setChallengeToken (String challengeToken) {
    this.challengeToken = challengeToken;
  }

  public String getEmail () {
    return email;
  }

  public void setEmail (String email) {
    this.email = email;
  }

  public long getExpiresInSeconds () {
    return expiresInSeconds;
  }

  public void setExpiresInSeconds (long expiresInSeconds) {
    this.expiresInSeconds = expiresInSeconds;
  }

  public long getResendAfterSeconds () {
    return resendAfterSeconds;
  }

  public void setResendAfterSeconds (long resendAfterSeconds) {
    this.resendAfterSeconds = resendAfterSeconds;
  }

  public String getMessage () {
    return message;
  }

  public void setMessage (String message) {
    this.message = message;
  }
}
