package com.hoaug.movieapi.modules.recommendation.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.recommendation.application.dto.request.UpsertMovieRecommendationRequest;
import com.hoaug.movieapi.modules.recommendation.application.dto.response.MovieRecommendationResponse;
import com.hoaug.movieapi.modules.recommendation.application.mapper.MovieRecommendationMapper;
import com.hoaug.movieapi.modules.recommendation.domain.model.MovieRecommendation;
import com.hoaug.movieapi.modules.recommendation.domain.repository.MovieRecommendationRepository;

@Component
public class UpsertMovieRecommendationUseCase {

  private final MovieRecommendationRepository movieRecommendationRepository;
  private final MovieRepository movieRepository;
  private final MovieRecommendationMapper movieRecommendationMapper;

  public UpsertMovieRecommendationUseCase(
      MovieRecommendationRepository movieRecommendationRepository, MovieRepository movieRepository,
      MovieRecommendationMapper movieRecommendationMapper) {
    this.movieRecommendationRepository = movieRecommendationRepository;
    this.movieRepository = movieRepository;
    this.movieRecommendationMapper = movieRecommendationMapper;
  }

  public MovieRecommendationResponse execute (UpsertMovieRecommendationRequest request) {
    movieRepository.findById(request.getMovieId())
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    MovieRecommendation recommendation = movieRecommendationRepository
        .findByUserIdAndMovieId(request.getUserId(), request.getMovieId())
        .orElseGet(MovieRecommendation::new);

    recommendation.setUserId(request.getUserId());
    recommendation.setMovieId(request.getMovieId());
    recommendation.setScore(request.getScore());
    recommendation.setReason(request.getReason());

    if (recommendation.getCreatedAt() == null) {
      recommendation.setCreatedAt(LocalDateTime.now());
    }

    MovieRecommendation saved = movieRecommendationRepository.save(recommendation);
    return movieRecommendationMapper.toResponse(saved);
  }
}