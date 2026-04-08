package com.hoaug.movieapi.modules.reviewlike.presentation.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.reviewlike.application.dto.response.ReviewLikeResponse;
import com.hoaug.movieapi.modules.reviewlike.application.usecase.CheckReviewLikedUseCase;
import com.hoaug.movieapi.modules.reviewlike.application.usecase.ToggleReviewLikeUseCase;
import com.hoaug.movieapi.modules.reviewlike.application.usecase.UnlikeReviewUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

@RestController
@RequestMapping("${api.prefix:/api/v1}/review-likes")
public class ReviewLikeController {

  private final ToggleReviewLikeUseCase toggleReviewLikeUseCase;
  private final CheckReviewLikedUseCase checkReviewLikedUseCase;
  private final UnlikeReviewUseCase unlikeReviewUseCase;
  private final AuthUserRepository authUserRepository;

  public ReviewLikeController(ToggleReviewLikeUseCase toggleReviewLikeUseCase,
      CheckReviewLikedUseCase checkReviewLikedUseCase, UnlikeReviewUseCase unlikeReviewUseCase,
      AuthUserRepository authUserRepository) {
    this.toggleReviewLikeUseCase = toggleReviewLikeUseCase;
    this.checkReviewLikedUseCase = checkReviewLikedUseCase;
    this.unlikeReviewUseCase = unlikeReviewUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PostMapping("/{reviewId}")
  public ReviewLikeResponse toggle (Authentication authentication, @PathVariable Long reviewId) {
    return toggleReviewLikeUseCase.execute(getCurrentUserId(authentication), reviewId);
  }

  @DeleteMapping("/{reviewId}")
  public void unlike (Authentication authentication, @PathVariable Long reviewId) {
    unlikeReviewUseCase.execute(getCurrentUserId(authentication), reviewId);
  }

  @GetMapping("/{reviewId}/check")
  public ReviewLikeResponse check (Authentication authentication, @PathVariable Long reviewId) {
    return checkReviewLikedUseCase.execute(getCurrentUserId(authentication), reviewId);
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}