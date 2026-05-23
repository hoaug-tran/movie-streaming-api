package com.hoaug.movieapi.modules.chatbot.application;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class ChatRateLimiter {

  private static final Duration WINDOW = Duration.ofHours(1);
  private static final int MAX_BUCKETS = 4096;

  private final ChatbotProperties properties;
  private final Map<String, Deque<Instant>> buckets = new ConcurrentHashMap<>();

  public ChatRateLimiter(ChatbotProperties properties) {
    this.properties = properties;
  }

  public void enforce(ChatUserContext userContext, String clientIp) {
    String key = identityKey(userContext, clientIp);
    int limit = limitFor(userContext);
    if (limit <= 0) {
      return;
    }

    Instant now = Instant.now();
    Instant windowStart = now.minus(WINDOW);

    Deque<Instant> hits = buckets.computeIfAbsent(key, k -> new ArrayDeque<>());
    synchronized (hits) {
      while (!hits.isEmpty() && hits.peekFirst().isBefore(windowStart)) {
        hits.pollFirst();
      }
      if (hits.size() >= limit) {
        throw new ChatRateLimitException(
            "Bạn đã đạt giới hạn hội thoại trong giờ này. Vui lòng thử lại sau ít phút.");
      }
      hits.addLast(now);
    }

    if (buckets.size() > MAX_BUCKETS) {
      buckets.entrySet().removeIf(entry -> {
        Deque<Instant> q = entry.getValue();
        synchronized (q) {
          return q.isEmpty() || q.peekLast().isBefore(windowStart);
        }
      });
    }
  }

  private int limitFor(ChatUserContext userContext) {
    if (userContext != null && userContext.authenticated()) {
      return properties.getRateLimitPerHour();
    }
    return properties.getGuestRateLimitPerHour();
  }

  private String identityKey(ChatUserContext userContext, String clientIp) {
    if (userContext != null && userContext.authenticated() && userContext.userId() != null) {
      return "user:" + userContext.userId();
    }
    String ip = (clientIp == null || clientIp.isBlank()) ? "unknown" : clientIp;
    return "guest:" + ip;
  }
}
