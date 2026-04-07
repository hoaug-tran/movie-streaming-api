package com.hoaug.movieapi.modules.recommendation.application.usecase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieSummaryResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMoviesUseCase;
import com.hoaug.movieapi.modules.recommendation.application.dto.request.GenerateRecommendationsRequest;
import com.hoaug.movieapi.modules.recommendation.application.dto.response.MovieRecommendationResponse;
import com.hoaug.movieapi.modules.recommendation.application.mapper.MovieRecommendationMapper;
import com.hoaug.movieapi.modules.recommendation.domain.model.MovieRecommendation;
import com.hoaug.movieapi.modules.recommendation.domain.repository.MovieRecommendationRepository;

// TODO: Tích hợp AI

@Component
public class GenerateRecommendationsUseCase {

  private final MovieRecommendationRepository movieRecommendationRepository;
  private final GetMoviesUseCase getMoviesUseCase;
  private final MovieRecommendationMapper movieRecommendationMapper;

  public GenerateRecommendationsUseCase(MovieRecommendationRepository movieRecommendationRepository,
      GetMoviesUseCase getMoviesUseCase, MovieRecommendationMapper movieRecommendationMapper) {
    this.movieRecommendationRepository = movieRecommendationRepository;
    this.getMoviesUseCase = getMoviesUseCase;
    this.movieRecommendationMapper = movieRecommendationMapper;
  }

  public List<MovieRecommendationResponse> execute (GenerateRecommendationsRequest request) {
    int limit = request.getLimit() == null || request.getLimit() <= 0 ? 10 : request.getLimit();

    movieRecommendationRepository.deleteAllByUserId(request.getUserId());

    List<MovieSummaryResponse> movies = getMoviesUseCase.execute().stream().limit(limit).toList();

    for (int i = 0; i < movies.size(); i++) {
      MovieSummaryResponse movie = movies.get(i);

      MovieRecommendation recommendation = new MovieRecommendation();
      recommendation.setUserId(request.getUserId());
      recommendation.setMovieId(movie.getId());
      recommendation.setScore(BigDecimal.valueOf(Math.max(0.0, 10.0 - i)));
      recommendation.setReason("Phim hay đề cử");
      recommendation.setCreatedAt(LocalDateTime.now());

      movieRecommendationRepository.save(recommendation);
    }

    return movieRecommendationRepository
        .findByUserIdOrderByScoreDescCreatedAtDesc(request.getUserId()).stream()
        .map(movieRecommendationMapper::toResponse).toList();
  }
}