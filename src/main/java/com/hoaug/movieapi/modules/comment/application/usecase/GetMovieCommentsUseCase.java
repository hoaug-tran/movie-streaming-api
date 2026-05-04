package com.hoaug.movieapi.modules.comment.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.comment.application.mapper.CommentMapper;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;
import com.hoaug.movieapi.modules.commentlike.domain.repository.CommentLikeRepository;

@Component
public class GetMovieCommentsUseCase {

  private final CommentRepository commentRepository;
  private final CommentLikeRepository commentLikeRepository;
  private final CommentMapper commentMapper;

  public GetMovieCommentsUseCase(CommentRepository commentRepository,
      CommentLikeRepository commentLikeRepository, CommentMapper commentMapper) {
    this.commentRepository = commentRepository;
    this.commentLikeRepository = commentLikeRepository;
    this.commentMapper = commentMapper;
  }

  public List<CommentResponse> execute (Long movieId, Long currentUserId) {
    return commentRepository.findVisibleCommentsByMovieIdOrderByCreatedAtDesc(movieId).stream()
        .map(comment -> {
          CommentResponse response = commentMapper.toResponse(comment);
          long realLikeCount = commentLikeRepository.countByCommentId(comment.getId());
          response.setLikeCount(Math.toIntExact(realLikeCount));
          response.setLikedByCurrentUser(currentUserId != null
              && commentLikeRepository.existsByUserIdAndCommentId(currentUserId, comment.getId()));
          return response;
        }).toList();
  }
}