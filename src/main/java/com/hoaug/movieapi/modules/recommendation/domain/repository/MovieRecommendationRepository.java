package com.hoaug.movieapi.modules.recommendation.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.recommendation.domain.model.MovieRecommendation;

public interface MovieRecommendationRepository {

  Optional<MovieRecommendation> findByUserIdAndMovieId (Long userId, Long movieId);

  List<MovieRecommendation> findByUserIdOrderByScoreDescCreatedAtDesc (Long userId);

  MovieRecommendation save (MovieRecommendation recommendation);

  void delete (MovieRecommendation recommendation);

  void deleteAllByUserId (Long userId);
}