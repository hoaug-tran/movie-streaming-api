package com.hoaug.movieapi.modules.auth.application.dto.response;

public class LoginResult {
  private final AuthResponse authResponse;
  private final OtpChallengeResponse otpChallenge;

  public LoginResult(AuthResponse authResponse, OtpChallengeResponse otpChallenge) {
    this.authResponse = authResponse;
    this.otpChallenge = otpChallenge;
  }

  public boolean isDirectAuth () {
    return authResponse != null;
  }

  public AuthResponse getAuthResponse () {
    return authResponse;
  }

  public OtpChallengeResponse getOtpChallenge () {
    return otpChallenge;
  }
}
