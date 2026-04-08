package com.hoaug.movieapi.modules.commentlike.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.commentlike.domain.model.CommentLike;
import com.hoaug.movieapi.modules.commentlike.domain.repository.CommentLikeRepository;

@Component
public class UnlikeCommentUseCase {

  private final CommentLikeRepository commentLikeRepository;

  public UnlikeCommentUseCase(CommentLikeRepository commentLikeRepository) {
    this.commentLikeRepository = commentLikeRepository;
  }

  public void execute (Long userId, Long commentId) {
    CommentLike commentLike = commentLikeRepository.findByUserIdAndCommentId(userId, commentId)
        .orElseThrow( () -> new AppException(ErrorCode.COMMENT_LIKE_NOT_FOUND));
    commentLikeRepository.delete(commentLike);
  }
}
