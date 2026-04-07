package com.hoaug.movieapi.modules.review.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;
import com.hoaug.movieapi.modules.review.application.mapper.ReviewMapper;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;

@Component
public class GetMovieReviewsUseCase {

  private final ReviewRepository reviewRepository;
  private final ReviewMapper reviewMapper;

  public GetMovieReviewsUseCase(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
    this.reviewRepository = reviewRepository;
    this.reviewMapper = reviewMapper;
  }

  public List<ReviewResponse> execute (Long movieId) {
    return reviewRepository.findByMovieIdOrderByCreatedAtDesc(movieId).stream()
        .map(reviewMapper::toResponse).toList();
  }
}
