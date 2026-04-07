package com.hoaug.movieapi.common.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class BruteForceProtection {
  private final Map<String, LoginAttempt> attemptMap = new HashMap<>();
  private static final int MAX_ATTEMPTS = 5;
  private static final long LOCKOUT_DURATION = 300000;

  public boolean isLocked (String username) {
    LoginAttempt attempt = attemptMap.get(username);
    if (attempt == null) {
      return false;
    }

    if (System.currentTimeMillis() - attempt.lockoutTime > LOCKOUT_DURATION) {
      attemptMap.remove(username);
      return false;
    }

    return attempt.isLocked;
  }

  public void recordFailure (String username) {
    LoginAttempt attempt = attemptMap.computeIfAbsent(username, k -> new LoginAttempt());
    attempt.failureCount++;

    if (attempt.failureCount >= MAX_ATTEMPTS) {
      attempt.isLocked = true;
      attempt.lockoutTime = System.currentTimeMillis();
    }
  }

  public void recordSuccess (String username) {
    attemptMap.remove(username);
  }

  public int getFailureCount (String username) {
    LoginAttempt attempt = attemptMap.get(username);
    return attempt != null ? attempt.failureCount : 0;
  }

  public long getRemainingLockoutTime (String username) {
    LoginAttempt attempt = attemptMap.get(username);
    if (attempt == null || !attempt.isLocked) {
      return 0;
    }
    long remaining = LOCKOUT_DURATION - (System.currentTimeMillis() - attempt.lockoutTime);
    return Math.max(0, remaining);
  }

  private static class LoginAttempt {
    int failureCount = 0;
    boolean isLocked = false;
    long lockoutTime = 0;
  }
}
