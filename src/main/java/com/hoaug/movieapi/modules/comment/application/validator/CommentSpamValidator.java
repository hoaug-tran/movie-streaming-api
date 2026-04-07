package com.hoaug.movieapi.modules.comment.application.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;

@Component
public class CommentSpamValidator {
  private final Map<String, CommentRateLimit> rateLimitMap = new HashMap<>();
  private static final int MAX_COMMENTS_PER_MINUTE = 10;
  private static final long MINUTE_MILLIS = 60000;

  public void validate (Long userId, String content) {
    if (content == null || content.trim().length() < 3) {
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }

    if (hasRepeatingCharacters(content)) {
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }

    if (containsExcessiveLinks(content)) {
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }

    checkRateLimit(userId);
  }

  private void checkRateLimit (Long userId) {
    String key = "comment_" + userId;
    CommentRateLimit limit = rateLimitMap.computeIfAbsent(key, k -> new CommentRateLimit());

    long now = System.currentTimeMillis();
    if (now - limit.windowStart > MINUTE_MILLIS) {
      limit.count = 0;
      limit.windowStart = now;
    }

    if (limit.count >= MAX_COMMENTS_PER_MINUTE) {
      throw new AppException(ErrorCode.RATE_LIMIT_EXCEEDED);
    }

    limit.count++;
  }

  private boolean hasRepeatingCharacters (String content) {
    for (int i = 0; i < content.length() - 4; i++) {
      char c = content.charAt(i);
      if (content.charAt(i + 1) == c && content.charAt(i + 2) == c && content.charAt(i + 3) == c) {
        return true;
      }
    }
    return false;
  }

  private boolean containsExcessiveLinks (String content) {
    return content.toLowerCase().split("http").length > 3;
  }

  private static class CommentRateLimit {
    int count = 0;
    long windowStart = System.currentTimeMillis();
  }
}
