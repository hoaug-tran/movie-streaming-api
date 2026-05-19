package com.hoaug.movieapi.modules.recommendation.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.recommendation.application.dto.request.GenerateRecommendationsRequest;
import com.hoaug.movieapi.modules.recommendation.application.dto.request.UpsertMovieRecommendationRequest;
import com.hoaug.movieapi.modules.recommendation.application.dto.response.MovieRecommendationResponse;
import com.hoaug.movieapi.modules.recommendation.application.usecase.ClearUserRecommendationsUseCase;
import com.hoaug.movieapi.modules.recommendation.application.usecase.DeleteMovieRecommendationUseCase;
import com.hoaug.movieapi.modules.recommendation.application.usecase.GenerateRecommendationsUseCase;
import com.hoaug.movieapi.modules.recommendation.application.usecase.GetMyRecommendationsUseCase;
import com.hoaug.movieapi.modules.recommendation.application.usecase.UpsertMovieRecommendationUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/recommendations")
public class MovieRecommendationController {

  private final UpsertMovieRecommendationUseCase upsertMovieRecommendationUseCase;
  private final GetMyRecommendationsUseCase getMyRecommendationsUseCase;
  private final DeleteMovieRecommendationUseCase deleteMovieRecommendationUseCase;
  private final ClearUserRecommendationsUseCase clearUserRecommendationsUseCase;
  private final GenerateRecommendationsUseCase generateRecommendationsUseCase;
  private final AuthUserRepository authUserRepository;

  public MovieRecommendationController(
      UpsertMovieRecommendationUseCase upsertMovieRecommendationUseCase,
      GetMyRecommendationsUseCase getMyRecommendationsUseCase,
      DeleteMovieRecommendationUseCase deleteMovieRecommendationUseCase,
      ClearUserRecommendationsUseCase clearUserRecommendationsUseCase,
      GenerateRecommendationsUseCase generateRecommendationsUseCase,
      AuthUserRepository authUserRepository) {
    this.upsertMovieRecommendationUseCase = upsertMovieRecommendationUseCase;
    this.getMyRecommendationsUseCase = getMyRecommendationsUseCase;
    this.deleteMovieRecommendationUseCase = deleteMovieRecommendationUseCase;
    this.clearUserRecommendationsUseCase = clearUserRecommendationsUseCase;
    this.generateRecommendationsUseCase = generateRecommendationsUseCase;
    this.authUserRepository = authUserRepository;
  }

  @GetMapping("/me")
  public ResponseEntity<List<MovieRecommendationResponse>> getMyRecommendations (
      Authentication authentication) {
    return ResponseUtil.ok(getMyRecommendationsUseCase.execute(getCurrentUserId(authentication)).getItems());
  }

  @DeleteMapping("/me/{movieId}")
  public ResponseEntity<Void> deleteMyRecommendation (Authentication authentication,
      @PathVariable Long movieId) {
    deleteMovieRecommendationUseCase.execute(getCurrentUserId(authentication), movieId);
    return ResponseUtil.noContent();
  }

  @DeleteMapping("/me")
  public ResponseEntity<Void> clearMyRecommendations (Authentication authentication) {
    clearUserRecommendationsUseCase.execute(getCurrentUserId(authentication));
    return ResponseUtil.noContent();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<MovieRecommendationResponse> upsert (
      @Valid @RequestBody UpsertMovieRecommendationRequest request) {
    return ResponseUtil.created(upsertMovieRecommendationUseCase.execute(request));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/generate")
  public ResponseEntity<List<MovieRecommendationResponse>> generate (
      @Valid @RequestBody GenerateRecommendationsRequest request) {
    return ResponseUtil.ok(generateRecommendationsUseCase.execute(request));
  }

  @PostMapping("/me/refresh")
  public ResponseEntity<List<MovieRecommendationResponse>> refreshMyRecommendations (
      Authentication authentication) {
    GenerateRecommendationsRequest request = new GenerateRecommendationsRequest();
    request.setUserId(getCurrentUserId(authentication));
    request.setLimit(12);
    return ResponseUtil.ok(generateRecommendationsUseCase.execute(request));
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}