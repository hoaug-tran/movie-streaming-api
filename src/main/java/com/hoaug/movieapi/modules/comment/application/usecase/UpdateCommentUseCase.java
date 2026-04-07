package com.hoaug.movieapi.modules.comment.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.comment.application.dto.request.UpdateCommentRequest;
import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.comment.application.mapper.CommentMapper;
import com.hoaug.movieapi.modules.comment.domain.model.Comment;
import com.hoaug.movieapi.modules.comment.domain.model.CommentStatus;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;

@Component
public class UpdateCommentUseCase {

  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  public UpdateCommentUseCase(CommentRepository commentRepository, CommentMapper commentMapper) {
    this.commentRepository = commentRepository;
    this.commentMapper = commentMapper;
  }

  public CommentResponse execute (Long userId, Long commentId, UpdateCommentRequest request) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow( () -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

    if (!comment.getUserId().equals(userId)) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    if (comment.getStatus() != CommentStatus.VISIBLE) {
      throw new AppException(ErrorCode.COMMENT_NOT_EDITABLE);
    }

    comment.setContent(request.getContent());
    comment.setUpdatedAt(LocalDateTime.now());

    Comment savedComment = commentRepository.save(comment);
    return commentMapper.toResponse(savedComment);
  }
}