package com.hoaug.movieapi.modules.review.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;
import com.hoaug.movieapi.modules.review.application.mapper.ReviewMapper;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;

@Component
public class GetMyReviewsUseCase {

  private final ReviewRepository reviewRepository;
  private final ReviewMapper reviewMapper;

  public GetMyReviewsUseCase(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
    this.reviewRepository = reviewRepository;
    this.reviewMapper = reviewMapper;
  }

  public List<ReviewResponse> execute (Long userId) {
    return reviewRepository.findByUserId(userId).stream().map(reviewMapper::toResponse).toList();
  }
}
