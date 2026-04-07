package com.hoaug.movieapi.modules.review.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.review.domain.model.Review;

public interface ReviewRepository {
  Optional<Review> findById (Long id);

  Optional<Review> findByUserIdAndMovieId (Long userId, Long movieId);

  List<Review> findByMovieIdOrderByCreatedAtDesc (Long movieId);

  List<Review> findByUserId (Long userId);

  Review save (Review review);

  void delete (Review review);

  void increaseLikeCount (Long reviewId);

  void decreaseLikeCount (Long reviewId);
}