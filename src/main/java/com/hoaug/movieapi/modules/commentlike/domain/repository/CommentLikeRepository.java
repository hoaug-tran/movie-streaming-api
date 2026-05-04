package com.hoaug.movieapi.modules.commentlike.domain.repository;

import java.util.Optional;

import com.hoaug.movieapi.modules.commentlike.domain.model.CommentLike;

public interface CommentLikeRepository {

  Optional<CommentLike> findByUserIdAndCommentId (Long userId, Long commentId);

  boolean existsByUserIdAndCommentId (Long userId, Long commentId);

  long countByCommentId (Long commentId);

  CommentLike save (CommentLike commentLike);

  void delete (CommentLike commentLike);
}