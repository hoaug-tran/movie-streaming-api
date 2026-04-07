package com.hoaug.movieapi.common.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;

@Component
public class LikeSpamValidator {
  private final Map<String, LikeRateLimit> rateLimitMap = new HashMap<>();
  private static final int MAX_LIKES_PER_SECOND = 10;
  private static final long SECOND_MILLIS = 1000;

  public void validateReviewLike (Long userId, Long reviewId) {
    checkRateLimit(userId, "review_like");
  }

  public void validateCommentLike (Long userId, Long commentId) {
    checkRateLimit(userId, "comment_like");
  }

  private void checkRateLimit (Long userId, String type) {
    String key = type + "_" + userId;
    LikeRateLimit limit = rateLimitMap.computeIfAbsent(key, k -> new LikeRateLimit());

    long now = System.currentTimeMillis();
    if (now - limit.windowStart > SECOND_MILLIS) {
      limit.count = 0;
      limit.windowStart = now;
    }

    if (limit.count >= MAX_LIKES_PER_SECOND) {
      throw new AppException(ErrorCode.RATE_LIMIT_EXCEEDED);
    }

    limit.count++;
  }

  private static class LikeRateLimit {
    int count = 0;
    long windowStart = System.currentTimeMillis();
  }
}
