package com.hoaug.movieapi.modules.reviewlike.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.reviewlike.infrastructure.persistence.entity.ReviewLikeEntity;

public interface JpaReviewLikeRepository extends JpaRepository<ReviewLikeEntity, Long> {

  Optional<ReviewLikeEntity> findByUserIdAndReviewId (Long userId, Long reviewId);
}