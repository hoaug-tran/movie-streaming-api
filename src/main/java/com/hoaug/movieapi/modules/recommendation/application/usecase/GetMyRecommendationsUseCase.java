package com.hoaug.movieapi.modules.recommendation.application.usecase;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.recommendation.application.dto.response.MovieRecommendationResponse;
import com.hoaug.movieapi.modules.recommendation.application.dto.response.RecommendationListResponse;
import com.hoaug.movieapi.modules.recommendation.application.mapper.MovieRecommendationMapper;
import com.hoaug.movieapi.modules.recommendation.domain.repository.MovieRecommendationRepository;

@Component
public class GetMyRecommendationsUseCase {

  private final MovieRecommendationRepository recommendationRepository;
  private final MovieRecommendationMapper recommendationMapper;
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public GetMyRecommendationsUseCase(MovieRecommendationRepository recommendationRepository,
      MovieRecommendationMapper recommendationMapper, MovieRepository movieRepository,
      MovieMapper movieMapper) {
    this.recommendationRepository = recommendationRepository;
    this.recommendationMapper = recommendationMapper;
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  @Cacheable(cacheNames = "recommendations", key = "'user:' + #userId + ':recommendations'")
  public RecommendationListResponse execute (Long userId) {
    List<MovieRecommendationResponse> items = recommendationRepository
        .findByUserIdOrderByScoreDescCreatedAtDesc(userId).stream()
        .map(recommendation -> {
          MovieRecommendationResponse res = recommendationMapper.toResponse(recommendation);
          movieRepository.findById(recommendation.getMovieId()).ifPresent(movie -> {
            res.setMovie(movieMapper.toBasicResponse(movie));
          });
          return res;
        }).toList();
    return new RecommendationListResponse(items);
  }
}