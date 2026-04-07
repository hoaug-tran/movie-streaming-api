package com.hoaug.movieapi.modules.reviewlike.domain.repository;

import java.util.Optional;

import com.hoaug.movieapi.modules.reviewlike.domain.model.ReviewLike;

public interface ReviewLikeRepository {

  Optional<ReviewLike> findByUserIdAndReviewId (Long userId, Long reviewId);

  ReviewLike save (ReviewLike reviewLike);

  void delete (ReviewLike reviewLike);
}