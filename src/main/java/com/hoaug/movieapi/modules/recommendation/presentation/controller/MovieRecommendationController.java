package com.hoaug.movieapi.modules.recommendation.presentation.controller;

import java.util.List;

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
  public List<MovieRecommendationResponse> getMyRecommendations (Authentication authentication) {
    return getMyRecommendationsUseCase.execute(getCurrentUserId(authentication));
  }

  @DeleteMapping("/me/{movieId}")
  public void deleteMyRecommendation (Authentication authentication, @PathVariable Long movieId) {
    deleteMovieRecommendationUseCase.execute(getCurrentUserId(authentication), movieId);
  }

  @DeleteMapping("/me")
  public void clearMyRecommendations (Authentication authentication) {
    clearUserRecommendationsUseCase.execute(getCurrentUserId(authentication));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public MovieRecommendationResponse upsert (
      @Valid @RequestBody UpsertMovieRecommendationRequest request) {
    return upsertMovieRecommendationUseCase.execute(request);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/generate")
  public List<MovieRecommendationResponse> generate (
      @Valid @RequestBody GenerateRecommendationsRequest request) {
    return generateRecommendationsUseCase.execute(request);
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}