package com.hoaug.movieapi.modules.review.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;
import com.hoaug.movieapi.modules.review.application.mapper.ReviewMapper;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;

@Component
public class GetReviewByIdUseCase {

  private final ReviewRepository reviewRepository;
  private final ReviewMapper reviewMapper;

  public GetReviewByIdUseCase(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
    this.reviewRepository = reviewRepository;
    this.reviewMapper = reviewMapper;
  }

  public ReviewResponse execute (Long reviewId) {
    return reviewRepository.findById(reviewId).map(reviewMapper::toResponse).orElse(null);
  }
}
