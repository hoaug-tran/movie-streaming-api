package com.hoaug.movieapi.modules.comment.presentation.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.comment.application.mapper.CommentMapper;
import com.hoaug.movieapi.modules.comment.application.usecase.GetMovieCommentsUseCase;
import com.hoaug.movieapi.modules.comment.domain.model.CommentStatus;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/comments")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCommentController {

  private final CommentRepository commentRepository;
  private final GetMovieCommentsUseCase getMovieCommentsUseCase;
  private final CommentMapper commentMapper;

  public AdminCommentController(CommentRepository commentRepository,
      GetMovieCommentsUseCase getMovieCommentsUseCase, CommentMapper commentMapper) {
    this.commentRepository = commentRepository;
    this.getMovieCommentsUseCase = getMovieCommentsUseCase;
    this.commentMapper = commentMapper;
  }

  @GetMapping("/movie/{movieId}")
  public List<CommentResponse> getMovieComments (@PathVariable Long movieId) {
    return getMovieCommentsUseCase.execute(movieId);
  }

  @PutMapping("/{id}/status")
  public CommentResponse updateCommentStatus (@PathVariable Long id,
      @RequestParam CommentStatus status) {
    var comment = commentRepository.findById(id)
        .orElseThrow( () -> new RuntimeException("Comment not found"));
    comment.setStatus(status);
    var updated = commentRepository.save(comment);
    return commentMapper.toResponse(updated);
  }

  @DeleteMapping("/{id}")
  public void deleteComment (@PathVariable Long id) {
    var comment = commentRepository.findById(id)
        .orElseThrow( () -> new RuntimeException("Comment not found"));
    commentRepository.delete(comment);
  }
}
