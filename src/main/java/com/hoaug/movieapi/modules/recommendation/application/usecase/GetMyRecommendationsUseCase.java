package com.hoaug.movieapi.modules.recommendation.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.recommendation.application.dto.response.MovieRecommendationResponse;
import com.hoaug.movieapi.modules.recommendation.application.mapper.MovieRecommendationMapper;
import com.hoaug.movieapi.modules.recommendation.domain.repository.MovieRecommendationRepository;

@Component
public class GetMyRecommendationsUseCase {

  private final MovieRecommendationRepository movieRecommendationRepository;
  private final MovieRecommendationMapper movieRecommendationMapper;

  public GetMyRecommendationsUseCase(MovieRecommendationRepository movieRecommendationRepository,
      MovieRecommendationMapper movieRecommendationMapper) {
    this.movieRecommendationRepository = movieRecommendationRepository;
    this.movieRecommendationMapper = movieRecommendationMapper;
  }

  public List<MovieRecommendationResponse> execute (Long userId) {
    return movieRecommendationRepository.findByUserIdOrderByScoreDescCreatedAtDesc(userId).stream()
        .map(movieRecommendationMapper::toResponse).toList();
  }
}