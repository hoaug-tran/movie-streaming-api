package com.hoaug.movieapi.modules.user.application.dto.response;

import java.time.LocalDateTime;

import com.hoaug.movieapi.modules.user.domain.model.EmailChangeStatus;

public class EmailChangeResponse {
  private String currentEmail;
  private String newEmail;
  private EmailChangeStatus status;
  private LocalDateTime expiresAt;

  public String getCurrentEmail () {
    return currentEmail;
  }

  public void setCurrentEmail (String currentEmail) {
    this.currentEmail = currentEmail;
  }

  public String getNewEmail () {
    return newEmail;
  }

  public void setNewEmail (String newEmail) {
    this.newEmail = newEmail;
  }

  public EmailChangeStatus getStatus () {
    return status;
  }

  public void setStatus (EmailChangeStatus status) {
    this.status = status;
  }

  public LocalDateTime getExpiresAt () {
    return expiresAt;
  }

  public void setExpiresAt (LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }
}
