package com.hoaug.movieapi.modules.recommendation.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.recommendation.domain.model.MovieRecommendation;
import com.hoaug.movieapi.modules.recommendation.domain.repository.MovieRecommendationRepository;
import com.hoaug.movieapi.modules.recommendation.infrastructure.persistence.entity.MovieRecommendationEntity;
import com.hoaug.movieapi.modules.recommendation.infrastructure.persistence.repository.JpaMovieRecommendationRepository;

@Component
public class MovieRecommendationRepositoryAdapter implements MovieRecommendationRepository {

  private final JpaMovieRecommendationRepository jpaMovieRecommendationRepository;

  public MovieRecommendationRepositoryAdapter(
      JpaMovieRecommendationRepository jpaMovieRecommendationRepository) {
    this.jpaMovieRecommendationRepository = jpaMovieRecommendationRepository;
  }

  @Override
  public Optional<MovieRecommendation> findByUserIdAndMovieId (Long userId, Long movieId) {
    return jpaMovieRecommendationRepository.findByUserIdAndMovieId(userId, movieId)
        .map(this::toDomain);
  }

  @Override
  public List<MovieRecommendation> findByUserIdOrderByScoreDescCreatedAtDesc (Long userId) {
    return jpaMovieRecommendationRepository.findByUserIdOrderByScoreDescCreatedAtDesc(userId)
        .stream().map(this::toDomain).toList();
  }

  @Override
  public MovieRecommendation save (MovieRecommendation recommendation) {
    MovieRecommendationEntity savedEntity = jpaMovieRecommendationRepository
        .save(toEntity(recommendation));
    return toDomain(savedEntity);
  }

  @Override
  public void delete (MovieRecommendation recommendation) {
    jpaMovieRecommendationRepository.delete(toEntity(recommendation));
  }

  @Override
  public void deleteAllByUserId (Long userId) {
    jpaMovieRecommendationRepository.deleteAllByUserId(userId);
  }

  private MovieRecommendation toDomain (MovieRecommendationEntity entity) {
    MovieRecommendation recommendation = new MovieRecommendation();
    recommendation.setId(entity.getId());
    recommendation.setUserId(entity.getUserId());
    recommendation.setMovieId(entity.getMovieId());
    recommendation.setScore(entity.getScore());
    recommendation.setReason(entity.getReason());
    recommendation.setCreatedAt(entity.getCreatedAt());
    return recommendation;
  }

  private MovieRecommendationEntity toEntity (MovieRecommendation recommendation) {
    MovieRecommendationEntity entity = new MovieRecommendationEntity();
    entity.setId(recommendation.getId());
    entity.setUserId(recommendation.getUserId());
    entity.setMovieId(recommendation.getMovieId());
    entity.setScore(recommendation.getScore());
    entity.setReason(recommendation.getReason());
    entity.setCreatedAt(recommendation.getCreatedAt());
    return entity;
  }
}