package com.hoaug.movieapi.modules.review.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hoaug.movieapi.modules.review.infrastructure.persistence.entity.ReviewEntity;

public interface JpaReviewRepository extends JpaRepository<ReviewEntity, Long> {
  Optional<ReviewEntity> findByUserIdAndMovieId (Long userId, Long movieId);

  List<ReviewEntity> findByMovieIdOrderByCreatedAtDesc (Long movieId);

  Page<ReviewEntity> findByMovieIdOrderByCreatedAtDesc (Long movieId, Pageable pageable);

  List<ReviewEntity> findByUserId (Long userId);

  @Modifying
  @Query("""
          update ReviewEntity r
          set r.likeCount = r.likeCount + 1
          where r.id = :reviewId
      """)
  void increaseLikeCount (Long reviewId);

  @Modifying
  @Query("""
          update ReviewEntity r
          set r.likeCount = case when r.likeCount > 0 then r.likeCount - 1 else 0 end
          where r.id = :reviewId
      """)
  void decreaseLikeCount (Long reviewId);
}