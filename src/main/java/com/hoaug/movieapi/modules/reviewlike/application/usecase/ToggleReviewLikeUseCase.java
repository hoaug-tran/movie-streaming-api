package com.hoaug.movieapi.modules.reviewlike.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;
import com.hoaug.movieapi.modules.reviewlike.application.dto.response.ReviewLikeResponse;
import com.hoaug.movieapi.modules.reviewlike.application.mapper.ReviewLikeMapper;
import com.hoaug.movieapi.modules.reviewlike.domain.model.ReviewLike;
import com.hoaug.movieapi.modules.reviewlike.domain.repository.ReviewLikeRepository;

import jakarta.transaction.Transactional;

@Component
public class ToggleReviewLikeUseCase {

  private final ReviewLikeRepository reviewLikeRepository;
  private final ReviewRepository reviewRepository;
  private final ReviewLikeMapper reviewLikeMapper;

  public ToggleReviewLikeUseCase(ReviewLikeRepository reviewLikeRepository,
      ReviewRepository reviewRepository, ReviewLikeMapper reviewLikeMapper) {
    this.reviewLikeRepository = reviewLikeRepository;
    this.reviewRepository = reviewRepository;
    this.reviewLikeMapper = reviewLikeMapper;
  }

  @Transactional
  public ReviewLikeResponse execute (Long userId, Long reviewId) {
    reviewRepository.findById(reviewId)
        .orElseThrow( () -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

    return reviewLikeRepository.findByUserIdAndReviewId(userId, reviewId).map(existingLike -> {
      reviewLikeRepository.delete(existingLike);
      reviewRepository.decreaseLikeCount(reviewId);
      return reviewLikeMapper.toResponse(reviewId, false);
    }).orElseGet( () -> {
      ReviewLike reviewLike = new ReviewLike();
      reviewLike.setUserId(userId);
      reviewLike.setReviewId(reviewId);
      reviewLike.setCreatedAt(LocalDateTime.now());
      reviewLikeRepository.save(reviewLike);
      reviewRepository.increaseLikeCount(reviewId);
      return reviewLikeMapper.toResponse(reviewId, true);
    });
  }
}