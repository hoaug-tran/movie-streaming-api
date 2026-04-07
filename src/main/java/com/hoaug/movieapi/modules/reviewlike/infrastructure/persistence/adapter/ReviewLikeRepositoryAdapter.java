package com.hoaug.movieapi.modules.reviewlike.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.reviewlike.domain.model.ReviewLike;
import com.hoaug.movieapi.modules.reviewlike.domain.repository.ReviewLikeRepository;
import com.hoaug.movieapi.modules.reviewlike.infrastructure.persistence.entity.ReviewLikeEntity;
import com.hoaug.movieapi.modules.reviewlike.infrastructure.persistence.repository.JpaReviewLikeRepository;

@Component
public class ReviewLikeRepositoryAdapter implements ReviewLikeRepository {

  private final JpaReviewLikeRepository jpaReviewLikeRepository;

  public ReviewLikeRepositoryAdapter(JpaReviewLikeRepository jpaReviewLikeRepository) {
    this.jpaReviewLikeRepository = jpaReviewLikeRepository;
  }

  @Override
  public Optional<ReviewLike> findByUserIdAndReviewId (Long userId, Long reviewId) {
    return jpaReviewLikeRepository.findByUserIdAndReviewId(userId, reviewId).map(this::toDomain);
  }

  @Override
  public ReviewLike save (ReviewLike reviewLike) {
    ReviewLikeEntity savedEntity = jpaReviewLikeRepository.save(toEntity(reviewLike));
    return toDomain(savedEntity);
  }

  @Override
  public void delete (ReviewLike reviewLike) {
    jpaReviewLikeRepository.delete(toEntity(reviewLike));
  }

  private ReviewLike toDomain (ReviewLikeEntity entity) {
    ReviewLike reviewLike = new ReviewLike();
    reviewLike.setId(entity.getId());
    reviewLike.setUserId(entity.getUserId());
    reviewLike.setReviewId(entity.getReviewId());
    reviewLike.setCreatedAt(entity.getCreatedAt());
    return reviewLike;
  }

  private ReviewLikeEntity toEntity (ReviewLike reviewLike) {
    ReviewLikeEntity entity = new ReviewLikeEntity();
    entity.setId(reviewLike.getId());
    entity.setUserId(reviewLike.getUserId());
    entity.setReviewId(reviewLike.getReviewId());
    entity.setCreatedAt(reviewLike.getCreatedAt());
    return entity;
  }
}