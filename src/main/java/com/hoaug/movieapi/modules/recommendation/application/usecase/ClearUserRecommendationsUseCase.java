package com.hoaug.movieapi.modules.recommendation.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.recommendation.domain.repository.MovieRecommendationRepository;

@Component
public class ClearUserRecommendationsUseCase {

  private final MovieRecommendationRepository movieRecommendationRepository;

  public ClearUserRecommendationsUseCase(
      MovieRecommendationRepository movieRecommendationRepository) {
    this.movieRecommendationRepository = movieRecommendationRepository;
  }

  public void execute (Long userId) {
    movieRecommendationRepository.deleteAllByUserId(userId);
  }
}