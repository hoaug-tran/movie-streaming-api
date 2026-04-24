package com.hoaug.movieapi.modules.comment.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.comment.application.mapper.CommentMapper;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;

@Component
public class GetTopCommentsUseCase {

  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  public GetTopCommentsUseCase(CommentRepository commentRepository, CommentMapper commentMapper) {
    this.commentRepository = commentRepository;
    this.commentMapper = commentMapper;
  }

  public List<CommentResponse> execute (int limit) {
    return commentRepository.findTopComments(limit).stream()
        .map(commentMapper::toResponse).toList();
  }
}
