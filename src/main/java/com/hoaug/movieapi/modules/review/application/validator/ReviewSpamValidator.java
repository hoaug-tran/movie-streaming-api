package com.hoaug.movieapi.modules.review.application.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;

@Component
public class ReviewSpamValidator {
  private final ReviewRepository reviewRepository;
  private final Map<String, ReviewRateLimit> rateLimitMap = new HashMap<>();
  private static final int MAX_REVIEWS_PER_HOUR = 5;
  private static final long HOUR_MILLIS = 3600000;

  public ReviewSpamValidator(ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
  }

  public void validate (Long userId, Long movieId, String content, Integer rating) {
    if (content == null || content.trim().length() < 10) {
      throw new AppException(ErrorCode.REVIEW_CONTENT_TOO_SHORT);
    }

    if (rating == null || rating < 1 || rating > 5) {
      throw new AppException(ErrorCode.REVIEW_RATING_INVALID);
    }

    if (hasRepeatingCharacters(content)) {
      throw new AppException(ErrorCode.REVIEW_CONTENT_SPAM);
    }

    // KHÔNG còn block khi đã tồn tại review của user/movie:
    // - Endpoint `POST /api/v1/reviews` là cơ chế UPSERT (insert or update),
    //   user được phép sửa review của mình thoải mái. Trước đây check này đã
    //   chặn vĩnh viễn user đã từng review một phim không thể chỉnh sửa nữa,
    //   khiến frontend bị lỗi 400 VALIDATION_ERROR mà không hiểu nguyên nhân.

    checkRateLimit(userId);
  }

  private void checkRateLimit (Long userId) {
    String key = "review_" + userId;
    ReviewRateLimit limit = rateLimitMap.computeIfAbsent(key, k -> new ReviewRateLimit());

    long now = System.currentTimeMillis();
    if (now - limit.windowStart > HOUR_MILLIS) {
      limit.count = 0;
      limit.windowStart = now;
    }

    if (limit.count >= MAX_REVIEWS_PER_HOUR) {
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

  private static class ReviewRateLimit {
    int count = 0;
    long windowStart = System.currentTimeMillis();
  }
}
