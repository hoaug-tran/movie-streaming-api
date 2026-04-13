package com.hoaug.movieapi.modules.recommendation.infrastructure.persistence.entity;

import java.math.BigDecimal;

import com.hoaug.movieapi.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "movie_recommendations", uniqueConstraints = @UniqueConstraint(columnNames = {
    "user_id", "movie_id" }))
public class MovieRecommendationEntity extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "movie_id", nullable = false)
  private Long movieId;

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal score;

  @Column(length = 255)
  private String reason;

  public Long getUserId () {
    return userId;
  }

  public void setUserId (Long userId) {
    this.userId = userId;
  }

  public Long getMovieId () {
    return movieId;
  }

  public void setMovieId (Long movieId) {
    this.movieId = movieId;
  }

  public BigDecimal getScore () {
    return score;
  }

  public void setScore (BigDecimal score) {
    this.score = score;
  }

  public String getReason () {
    return reason;
  }

  public void setReason (String reason) {
    this.reason = reason;
  }
}