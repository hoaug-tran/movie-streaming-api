package com.hoaug.movieapi.modules.recommendation.application.dto.request;

import java.math.BigDecimal;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class UpsertMovieRecommendationRequest {

  @NotNull(message = "User ID is required")
  @Positive(message = "User ID must be positive")
  private Long userId;

  @NotNull(message = "Movie ID is required")
  @Positive(message = "Movie ID must be positive")
  private Long movieId;

  @NotNull(message = "Recommendation score is required")
  @DecimalMin(value = "0.00", message = "Score must be at least 0.00")
  private BigDecimal score;

  @Size(max = 255, message = "Reason must be at most 255 characters")
  @ValidSafeString(minLength = 0, maxLength = 255)
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