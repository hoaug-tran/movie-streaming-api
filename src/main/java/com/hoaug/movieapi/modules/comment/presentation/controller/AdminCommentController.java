package com.hoaug.movieapi.modules.comment.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.comment.application.dto.request.AdminCommentRequest;
import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.comment.application.mapper.CommentMapper;
import com.hoaug.movieapi.modules.comment.application.usecase.AdminCreateCommentUseCase;
import com.hoaug.movieapi.modules.comment.application.usecase.AdminUpdateCommentUseCase;
import com.hoaug.movieapi.modules.comment.application.usecase.GetMovieCommentsUseCase;
import com.hoaug.movieapi.modules.comment.domain.model.CommentStatus;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/comments")
public class AdminCommentController {

  private final CommentRepository commentRepository;
  private final GetMovieCommentsUseCase getMovieCommentsUseCase;
  private final CommentMapper commentMapper;
  private final AdminCreateCommentUseCase adminCreateCommentUseCase;
  private final AdminUpdateCommentUseCase adminUpdateCommentUseCase;

  public AdminCommentController(CommentRepository commentRepository,
      GetMovieCommentsUseCase getMovieCommentsUseCase, CommentMapper commentMapper,
      AdminCreateCommentUseCase adminCreateCommentUseCase,
      AdminUpdateCommentUseCase adminUpdateCommentUseCase) {
    this.commentRepository = commentRepository;
    this.getMovieCommentsUseCase = getMovieCommentsUseCase;
    this.commentMapper = commentMapper;
    this.adminCreateCommentUseCase = adminCreateCommentUseCase;
    this.adminUpdateCommentUseCase = adminUpdateCommentUseCase;
  }

  @GetMapping("")
  @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
  public ResponseEntity<List<CommentResponse>> getAllComments () {
    return ResponseUtil.ok(commentRepository.findAll().stream().map(commentMapper::toResponse).toList());
  }

  @GetMapping("/movie/{movieId}")
  @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
  public ResponseEntity<List<CommentResponse>> getMovieComments (@PathVariable Long movieId) {
    return ResponseUtil.ok(getMovieCommentsUseCase.execute(movieId, null));
  }

  @PostMapping("")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<CommentResponse> createComment (
      @Valid @RequestBody AdminCommentRequest request) {
    return ResponseUtil.created(adminCreateCommentUseCase.execute(request));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<CommentResponse> updateComment (@PathVariable Long id,
      @Valid @RequestBody AdminCommentRequest request) {
    return ResponseUtil.ok(adminUpdateCommentUseCase.execute(id, request));
  }

  @PutMapping("/{id}/status")
  @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
  public ResponseEntity<CommentResponse> updateCommentStatus (@PathVariable Long id,
      @RequestParam CommentStatus status) {
    var comment = commentRepository.findById(id)
        .orElseThrow( () -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
    comment.setStatus(status);
    var updated = commentRepository.save(comment);
    return ResponseUtil.ok(commentMapper.toResponse(updated));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
  public ResponseEntity<Void> deleteComment (@PathVariable Long id) {
    var comment = commentRepository.findById(id)
        .orElseThrow( () -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
    commentRepository.delete(comment);
    return ResponseUtil.noContent();
  }
}
