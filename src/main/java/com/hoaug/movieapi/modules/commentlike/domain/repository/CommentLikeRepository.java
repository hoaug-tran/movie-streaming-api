package com.hoaug.movieapi.modules.commentlike.domain.repository;

import java.util.Optional;

import com.hoaug.movieapi.modules.commentlike.domain.model.CommentLike;

public interface CommentLikeRepository {

  Optional<CommentLike> findByUserIdAndCommentId (Long userId, Long commentId);

  CommentLike save (CommentLike commentLike);

  void delete (CommentLike commentLike);
}