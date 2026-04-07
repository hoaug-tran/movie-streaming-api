package com.hoaug.movieapi.modules.recommendation.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.recommendation.domain.repository.MovieRecommendationRepository;

@Component
public class DeleteMovieRecommendationUseCase {

  private final MovieRecommendationRepository movieRecommendationRepository;

  public DeleteMovieRecommendationUseCase(
      MovieRecommendationRepository movieRecommendationRepository) {
    this.movieRecommendationRepository = movieRecommendationRepository;
  }

  public void execute (Long userId, Long movieId) {
    movieRecommendationRepository.findByUserIdAndMovieId(userId, movieId)
        .ifPresent(movieRecommendationRepository::delete);
  }
}