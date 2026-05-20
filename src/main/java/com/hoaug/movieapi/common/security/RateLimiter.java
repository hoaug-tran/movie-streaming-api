package com.hoaug.movieapi.common.security;

import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class RateLimiter {
  private static final long DEFAULT_WINDOW_MILLIS = 60_000L;
  private static final long MAX_IDLE_MILLIS = 10 * 60_000L;
  private static final int CLEANUP_INTERVAL_REQUESTS = 1_000;

  private final Map<String, RateLimitInfo> limitMap = new ConcurrentHashMap<>();
  private int requestCounter;

  public boolean isAllowed(String key, int maxRequests) {
    return isAllowed(key, maxRequests, DEFAULT_WINDOW_MILLIS);
  }

  public boolean isAllowed(String key, int maxRequests, long windowMillis) {
    long now = Instant.now().toEpochMilli();
    cleanupIfNeeded(now);
    RateLimitInfo info = limitMap.computeIfAbsent(key, ignored -> new RateLimitInfo(now));

    synchronized (info) {
      if (now - info.windowStart >= windowMillis) {
        info.requestCount = 0;
        info.windowStart = now;
      }

      info.lastSeen = now;
      info.requestCount++;
      return info.requestCount <= maxRequests;
    }
  }

  private synchronized void cleanupIfNeeded(long now) {
    requestCounter++;
    if (requestCounter < CLEANUP_INTERVAL_REQUESTS) {
      return;
    }
    requestCounter = 0;

    Iterator<Map.Entry<String, RateLimitInfo>> iterator = limitMap.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, RateLimitInfo> entry = iterator.next();
      if (now - entry.getValue().lastSeen > MAX_IDLE_MILLIS) {
        iterator.remove();
      }
    }
  }

  private static class RateLimitInfo {
    int requestCount;
    long windowStart;
    long lastSeen;

    RateLimitInfo(long now) {
      this.windowStart = now;
      this.lastSeen = now;
    }
  }
}
