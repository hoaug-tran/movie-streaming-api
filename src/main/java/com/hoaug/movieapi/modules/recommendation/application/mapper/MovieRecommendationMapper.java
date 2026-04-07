package com.hoaug.movieapi.modules.recommendation.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.recommendation.application.dto.response.MovieRecommendationResponse;
import com.hoaug.movieapi.modules.recommendation.domain.model.MovieRecommendation;

@Component
public class MovieRecommendationMapper {

  public MovieRecommendationResponse toResponse (MovieRecommendation recommendation) {
    MovieRecommendationResponse response = new MovieRecommendationResponse();
    response.setId(recommendation.getId());
    response.setMovieId(recommendation.getMovieId());
    response.setScore(recommendation.getScore());
    response.setReason(recommendation.getReason());
    response.setCreatedAt(recommendation.getCreatedAt());
    return response;
  }
}