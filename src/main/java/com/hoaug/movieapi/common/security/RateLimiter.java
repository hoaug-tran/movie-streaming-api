package com.hoaug.movieapi.common.security;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class RateLimiter {
  private final Map<String, RateLimitInfo> limitMap = new HashMap<>();
  private static final long RATE_LIMIT_WINDOW = 60000;

  public boolean isAllowed (String key, int maxRequests) {
    RateLimitInfo info = limitMap.computeIfAbsent(key, k -> new RateLimitInfo());

    long now = Instant.now().toEpochMilli();
    if (now - info.windowStart > RATE_LIMIT_WINDOW) {
      info.requestCount = 0;
      info.windowStart = now;
    }

    info.requestCount++;
    return info.requestCount <= maxRequests;
  }

  private static class RateLimitInfo {
    int requestCount;
    long windowStart = Instant.now().toEpochMilli();
  }
}
