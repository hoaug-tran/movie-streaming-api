package com.hoaug.movieapi.modules.comment.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.comment.domain.model.Comment;
import com.hoaug.movieapi.modules.comment.domain.model.CommentStatus;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;

import jakarta.transaction.Transactional;

@Component
public class DeleteCommentUseCase {

  private final CommentRepository commentRepository;

  public DeleteCommentUseCase(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  @Transactional
  public void execute (Long userId, Long commentId) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow( () -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

    if (!comment.getUserId().equals(userId)) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    if (comment.getStatus() == CommentStatus.DELETED) {
      return;
    }

    comment.setStatus(CommentStatus.DELETED);
    comment.setContent("[deleted]");
    comment.setUpdatedAt(LocalDateTime.now());
    commentRepository.save(comment);

    if (comment.getParentCommentId() != null) {
      commentRepository.decreaseReplyCount(comment.getParentCommentId());
    }
  }
}