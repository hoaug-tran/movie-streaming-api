package com.hoaug.movieapi.modules.comment.presentation.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.comment.application.dto.request.CreateCommentRequest;
import com.hoaug.movieapi.modules.comment.application.dto.request.UpdateCommentRequest;
import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.comment.application.usecase.CreateCommentUseCase;
import com.hoaug.movieapi.modules.comment.application.usecase.DeleteCommentUseCase;
import com.hoaug.movieapi.modules.comment.application.usecase.GetMovieCommentsUseCase;
import com.hoaug.movieapi.modules.comment.application.usecase.GetRepliesUseCase;
import com.hoaug.movieapi.modules.comment.application.usecase.UpdateCommentUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/comments")
public class CommentController {

  private final CreateCommentUseCase createCommentUseCase;
  private final UpdateCommentUseCase updateCommentUseCase;
  private final DeleteCommentUseCase deleteCommentUseCase;
  private final GetMovieCommentsUseCase getMovieCommentsUseCase;
  private final GetRepliesUseCase getRepliesUseCase;
  private final AuthUserRepository authUserRepository;

  public CommentController(CreateCommentUseCase createCommentUseCase,
      UpdateCommentUseCase updateCommentUseCase, DeleteCommentUseCase deleteCommentUseCase,
      GetMovieCommentsUseCase getMovieCommentsUseCase, GetRepliesUseCase getRepliesUseCase,
      AuthUserRepository authUserRepository) {
    this.createCommentUseCase = createCommentUseCase;
    this.updateCommentUseCase = updateCommentUseCase;
    this.deleteCommentUseCase = deleteCommentUseCase;
    this.getMovieCommentsUseCase = getMovieCommentsUseCase;
    this.getRepliesUseCase = getRepliesUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PostMapping
  public CommentResponse create (Authentication authentication,
      @Valid @RequestBody CreateCommentRequest request) {
    return createCommentUseCase.execute(getCurrentUserId(authentication), request);
  }

  @PutMapping("/{commentId}")
  public CommentResponse update (Authentication authentication, @PathVariable Long commentId,
      @Valid @RequestBody UpdateCommentRequest request) {
    return updateCommentUseCase.execute(getCurrentUserId(authentication), commentId, request);
  }

  @DeleteMapping("/{commentId}")
  public void delete (Authentication authentication, @PathVariable Long commentId) {
    deleteCommentUseCase.execute(getCurrentUserId(authentication), commentId);
  }

  @GetMapping("/movie/{movieId}")
  public List<CommentResponse> getMovieComments (@PathVariable Long movieId) {
    return getMovieCommentsUseCase.execute(movieId);
  }

  @GetMapping("/{commentId}/replies")
  public List<CommentResponse> getReplies (@PathVariable Long commentId) {
    return getRepliesUseCase.execute(commentId);
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}