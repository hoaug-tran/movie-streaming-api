package com.hoaug.movieapi.modules.commentlike.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.commentlike.infrastructure.persistence.entity.CommentLikeEntity;

public interface JpaCommentLikeRepository extends JpaRepository<CommentLikeEntity, Long> {

  Optional<CommentLikeEntity> findByUserIdAndCommentId (Long userId, Long commentId);

  boolean existsByUserIdAndCommentId (Long userId, Long commentId);

  long countByCommentId (Long commentId);
}