package com.hoaug.movieapi.modules.comment.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.comment.application.mapper.CommentMapper;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;

@Component
public class GetCommentByIdUseCase {

  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  public GetCommentByIdUseCase(CommentRepository commentRepository, CommentMapper commentMapper) {
    this.commentRepository = commentRepository;
    this.commentMapper = commentMapper;
  }

  public CommentResponse execute (Long commentId) {
    return commentRepository.findById(commentId).map(commentMapper::toResponse).orElse(null);
  }
}
