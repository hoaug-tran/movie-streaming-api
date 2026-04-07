package com.hoaug.movieapi.modules.recommendation.application.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpsertMovieRecommendationRequest {

  @NotNull
  private Long userId;

  @NotNull
  private Long movieId;

  @NotNull
  @DecimalMin("0.00")
  private BigDecimal score;

  @Size(max = 255)
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