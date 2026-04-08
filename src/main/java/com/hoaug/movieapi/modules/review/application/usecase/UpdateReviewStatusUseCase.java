package com.hoaug.movieapi.modules.review.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.review.application.dto.request.UpdateReviewStatusRequest;
import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;
import com.hoaug.movieapi.modules.review.application.mapper.ReviewMapper;
import com.hoaug.movieapi.modules.review.domain.model.Review;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;

@Component
public class UpdateReviewStatusUseCase {

  private final ReviewRepository reviewRepository;
  private final ReviewMapper reviewMapper;

  public UpdateReviewStatusUseCase(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
    this.reviewRepository = reviewRepository;
    this.reviewMapper = reviewMapper;
  }

  public ReviewResponse execute (Long reviewId, UpdateReviewStatusRequest request) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow( () -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

    review.setStatus(request.getStatus());

    Review saved = reviewRepository.save(review);
    return reviewMapper.toResponse(saved);
  }
}
