package com.hoaug.movieapi.modules.commentlike.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;
import com.hoaug.movieapi.modules.commentlike.application.dto.response.CommentLikeResponse;
import com.hoaug.movieapi.modules.commentlike.application.mapper.CommentLikeMapper;
import com.hoaug.movieapi.modules.commentlike.domain.model.CommentLike;
import com.hoaug.movieapi.modules.commentlike.domain.repository.CommentLikeRepository;

import jakarta.transaction.Transactional;

@Component
public class ToggleCommentLikeUseCase {

  private final CommentLikeRepository commentLikeRepository;
  private final CommentRepository commentRepository;
  private final CommentLikeMapper commentLikeMapper;

  public ToggleCommentLikeUseCase(CommentLikeRepository commentLikeRepository,
      CommentRepository commentRepository, CommentLikeMapper commentLikeMapper) {
    this.commentLikeRepository = commentLikeRepository;
    this.commentRepository = commentRepository;
    this.commentLikeMapper = commentLikeMapper;
  }

  @Transactional
  public CommentLikeResponse execute (Long userId, Long commentId) {
    commentRepository.findById(commentId)
        .orElseThrow( () -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

    return commentLikeRepository.findByUserIdAndCommentId(userId, commentId).map(existingLike -> {
      commentLikeRepository.delete(existingLike);
      commentRepository.decreaseLikeCount(commentId);
      return commentLikeMapper.toResponse(commentId, false);
    }).orElseGet( () -> {
      CommentLike commentLike = new CommentLike();
      commentLike.setUserId(userId);
      commentLike.setCommentId(commentId);
      commentLike.setCreatedAt(LocalDateTime.now());
      commentLikeRepository.save(commentLike);
      commentRepository.increaseLikeCount(commentId);
      return commentLikeMapper.toResponse(commentId, true);
    });
  }
}