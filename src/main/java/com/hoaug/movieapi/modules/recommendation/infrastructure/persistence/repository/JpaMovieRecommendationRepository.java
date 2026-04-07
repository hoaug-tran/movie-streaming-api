package com.hoaug.movieapi.modules.recommendation.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.recommendation.infrastructure.persistence.entity.MovieRecommendationEntity;

public interface JpaMovieRecommendationRepository
    extends JpaRepository<MovieRecommendationEntity, Long> {

  Optional<MovieRecommendationEntity> findByUserIdAndMovieId (Long userId, Long movieId);

  List<MovieRecommendationEntity> findByUserIdOrderByScoreDescCreatedAtDesc (Long userId);

  void deleteAllByUserId (Long userId);
}