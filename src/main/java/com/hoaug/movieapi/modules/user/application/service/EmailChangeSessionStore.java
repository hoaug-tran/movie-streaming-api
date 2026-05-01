package com.hoaug.movieapi.modules.user.application.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.user.domain.model.EmailChangeStatus;

@Component
public class EmailChangeSessionStore {
  private final ConcurrentMap<Long, EmailChangeSession> sessions = new ConcurrentHashMap<>();

  public void save (EmailChangeSession session) {
    sessions.put(session.getUserId(), session);
  }

  public Optional<EmailChangeSession> find (Long userId, EmailChangeStatus status) {
    return Optional.ofNullable(sessions.get(userId)).filter(session -> session.getStatus() == status);
  }

  public void remove (Long userId) {
    sessions.remove(userId);
  }

  public static class EmailChangeSession {
    private Long userId;
    private String currentEmail;
    private String newEmail;
    private String currentEmailOtpHash;
    private String newEmailOtpHash;
    private EmailChangeStatus status;
    private LocalDateTime expiresAt;
    private int attemptCount;

    public Long getUserId () {
      return userId;
    }

    public void setUserId (Long userId) {
      this.userId = userId;
    }

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

    public String getCurrentEmailOtpHash () {
      return currentEmailOtpHash;
    }

    public void setCurrentEmailOtpHash (String currentEmailOtpHash) {
      this.currentEmailOtpHash = currentEmailOtpHash;
    }

    public String getNewEmailOtpHash () {
      return newEmailOtpHash;
    }

    public void setNewEmailOtpHash (String newEmailOtpHash) {
      this.newEmailOtpHash = newEmailOtpHash;
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

    public int getAttemptCount () {
      return attemptCount;
    }

    public void setAttemptCount (int attemptCount) {
      this.attemptCount = attemptCount;
    }
  }
}
