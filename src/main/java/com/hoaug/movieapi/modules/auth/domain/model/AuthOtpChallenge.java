package com.hoaug.movieapi.modules.auth.domain.model;

import java.time.LocalDateTime;

public class AuthOtpChallenge {
  private Long userId;
  private String email;
  private String purpose;
  private String challengeToken;
  private String otpHash;
  private String payload;
  private Integer attempts;
  private LocalDateTime expiresAt;
  private LocalDateTime createdAt;

  public Long getUserId () { return userId; }
  public void setUserId (Long userId) { this.userId = userId; }
  public String getEmail () { return email; }
  public void setEmail (String email) { this.email = email; }
  public String getPurpose () { return purpose; }
  public void setPurpose (String purpose) { this.purpose = purpose; }
  public String getChallengeToken () { return challengeToken; }
  public void setChallengeToken (String challengeToken) { this.challengeToken = challengeToken; }
  public String getOtpHash () { return otpHash; }
  public void setOtpHash (String otpHash) { this.otpHash = otpHash; }
  public String getPayload () { return payload; }
  public void setPayload (String payload) { this.payload = payload; }
  public Integer getAttempts () { return attempts; }
  public void setAttempts (Integer attempts) { this.attempts = attempts; }
  public LocalDateTime getExpiresAt () { return expiresAt; }
  public void setExpiresAt (LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
  public LocalDateTime getCreatedAt () { return createdAt; }
  public void setCreatedAt (LocalDateTime createdAt) { this.createdAt = createdAt; }
}
