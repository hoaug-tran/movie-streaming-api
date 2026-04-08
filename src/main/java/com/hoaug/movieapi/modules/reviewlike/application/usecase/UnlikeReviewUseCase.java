package com.hoaug.movieapi.modules.reviewlike.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.reviewlike.domain.model.ReviewLike;
import com.hoaug.movieapi.modules.reviewlike.domain.repository.ReviewLikeRepository;

@Component
public class UnlikeReviewUseCase {

  private final ReviewLikeRepository reviewLikeRepository;

  public UnlikeReviewUseCase(ReviewLikeRepository reviewLikeRepository) {
    this.reviewLikeRepository = reviewLikeRepository;
  }

  public void execute (Long userId, Long reviewId) {
    ReviewLike reviewLike = reviewLikeRepository.findByUserIdAndReviewId(userId, reviewId)
        .orElseThrow( () -> new AppException(ErrorCode.REVIEW_LIKE_NOT_FOUND));
    reviewLikeRepository.delete(reviewLike);
  }
}
