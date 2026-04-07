package com.hoaug.movieapi.modules.reviewlike.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.reviewlike.application.dto.response.ReviewLikeResponse;
import com.hoaug.movieapi.modules.reviewlike.application.mapper.ReviewLikeMapper;
import com.hoaug.movieapi.modules.reviewlike.domain.repository.ReviewLikeRepository;

@Component
public class CheckReviewLikedUseCase {

  private final ReviewLikeRepository reviewLikeRepository;
  private final ReviewLikeMapper reviewLikeMapper;

  public CheckReviewLikedUseCase(ReviewLikeRepository reviewLikeRepository,
      ReviewLikeMapper reviewLikeMapper) {
    this.reviewLikeRepository = reviewLikeRepository;
    this.reviewLikeMapper = reviewLikeMapper;
  }

  public ReviewLikeResponse execute (Long userId, Long reviewId) {
    boolean liked = reviewLikeRepository.findByUserIdAndReviewId(userId, reviewId).isPresent();
    return reviewLikeMapper.toResponse(reviewId, liked);
  }
}