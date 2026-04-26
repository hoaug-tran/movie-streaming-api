package com.hoaug.movieapi.modules.recommendation.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieBasicResponse;

public class MovieRecommendationResponse {

  private Long id;
  private Long movieId;
  private BigDecimal score;
  private String reason;
  private LocalDateTime createdAt;
  private MovieBasicResponse movie;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
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

  public MovieBasicResponse getMovie () {
    return movie;
  }

  public void setMovie (MovieBasicResponse movie) {
    this.movie = movie;
  }
}