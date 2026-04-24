package com.hoaug.movieapi.modules.recommendation.application.usecase;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.recommendation.application.dto.response.MovieRecommendationResponse;
import com.hoaug.movieapi.modules.recommendation.application.dto.response.RecommendationListResponse;
import com.hoaug.movieapi.modules.recommendation.application.mapper.MovieRecommendationMapper;
import com.hoaug.movieapi.modules.recommendation.domain.repository.MovieRecommendationRepository;

@Component
public class GetMyRecommendationsUseCase {

  private final MovieRecommendationRepository recommendationRepository;
  private final MovieRecommendationMapper recommendationMapper;

  public GetMyRecommendationsUseCase(MovieRecommendationRepository recommendationRepository,
      MovieRecommendationMapper recommendationMapper) {
    this.recommendationRepository = recommendationRepository;
    this.recommendationMapper = recommendationMapper;
  }

  @Cacheable(cacheNames = "recommendations", key = "'user:' + #userId + ':recommendations'")
  public RecommendationListResponse execute (Long userId) {
    List<MovieRecommendationResponse> items = recommendationRepository
        .findByUserIdOrderByScoreDescCreatedAtDesc(userId).stream()
        .map(recommendationMapper::toResponse).toList();
    return new RecommendationListResponse(items);
  }
}