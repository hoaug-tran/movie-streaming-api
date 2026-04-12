package com.hoaug.movieapi.modules.commentlike.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.commentlike.application.dto.response.CommentLikeResponse;
import com.hoaug.movieapi.modules.commentlike.application.usecase.CheckCommentLikedUseCase;
import com.hoaug.movieapi.modules.commentlike.application.usecase.ToggleCommentLikeUseCase;
import com.hoaug.movieapi.modules.commentlike.application.usecase.UnlikeCommentUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

@RestController
@RequestMapping("${api.prefix:/api/v1}/comment-likes")
public class CommentLikeController {

  private final ToggleCommentLikeUseCase toggleCommentLikeUseCase;
  private final CheckCommentLikedUseCase checkCommentLikedUseCase;
  private final UnlikeCommentUseCase unlikeCommentUseCase;
  private final AuthUserRepository authUserRepository;

  public CommentLikeController(ToggleCommentLikeUseCase toggleCommentLikeUseCase,
      CheckCommentLikedUseCase checkCommentLikedUseCase, UnlikeCommentUseCase unlikeCommentUseCase,
      AuthUserRepository authUserRepository) {
    this.toggleCommentLikeUseCase = toggleCommentLikeUseCase;
    this.checkCommentLikedUseCase = checkCommentLikedUseCase;
    this.unlikeCommentUseCase = unlikeCommentUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PostMapping("/{commentId}")
  public ResponseEntity<CommentLikeResponse> toggle (Authentication authentication,
      @PathVariable Long commentId) {
    CommentLikeResponse response = toggleCommentLikeUseCase
        .execute(getCurrentUserId(authentication), commentId);
    return ResponseUtil.created(response);
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> unlike (Authentication authentication, @PathVariable Long commentId) {
    unlikeCommentUseCase.execute(getCurrentUserId(authentication), commentId);
    return ResponseUtil.noContent();
  }

  @GetMapping("/{commentId}/check")
  public ResponseEntity<CommentLikeResponse> check (Authentication authentication,
      @PathVariable Long commentId) {
    CommentLikeResponse response = checkCommentLikedUseCase
        .execute(getCurrentUserId(authentication), commentId);
    return ResponseUtil.ok(response);
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}