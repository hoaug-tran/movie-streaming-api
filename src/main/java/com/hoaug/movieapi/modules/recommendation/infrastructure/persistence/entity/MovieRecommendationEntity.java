package com.hoaug.movieapi.modules.recommendation.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "movie_recommendations", uniqueConstraints = @UniqueConstraint(columnNames = {
    "user_id", "movie_id" }))
public class MovieRecommendationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "movie_id", nullable = false)
  private Long movieId;

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal score;

  @Column(length = 255)
  private String reason;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

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

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}