package com.hoaug.movieapi.modules.commentlike.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.commentlike.application.dto.response.CommentLikeResponse;
import com.hoaug.movieapi.modules.commentlike.application.mapper.CommentLikeMapper;
import com.hoaug.movieapi.modules.commentlike.domain.repository.CommentLikeRepository;

@Component
public class CheckCommentLikedUseCase {

  private final CommentLikeRepository commentLikeRepository;
  private final CommentLikeMapper commentLikeMapper;

  public CheckCommentLikedUseCase(CommentLikeRepository commentLikeRepository,
      CommentLikeMapper commentLikeMapper) {
    this.commentLikeRepository = commentLikeRepository;
    this.commentLikeMapper = commentLikeMapper;
  }

  public CommentLikeResponse execute (Long userId, Long commentId) {
    boolean liked = commentLikeRepository.findByUserIdAndCommentId(userId, commentId).isPresent();
    return commentLikeMapper.toResponse(commentId, liked);
  }
}