package com.hoaug.movieapi.modules.review.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;

@Component
public class DeleteReviewUseCase {

  private final ReviewRepository reviewRepository;

  public DeleteReviewUseCase(ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
  }

  public void execute (Long userId, Long reviewId) {
    var review = reviewRepository.findById(reviewId)
        .orElseThrow( () -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

    if (!review.getUserId().equals(userId)) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    reviewRepository.delete(review);
  }
}
