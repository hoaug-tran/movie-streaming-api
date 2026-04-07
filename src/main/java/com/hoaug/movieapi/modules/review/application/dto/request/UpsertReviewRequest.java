package com.hoaug.movieapi.modules.review.application.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpsertReviewRequest {

  @NotNull
  private Long movieId;

  @NotNull
  @Min(1)
  @Max(5)
  private Integer rating;

  private String content;

  public Long getMovieId () {
    return movieId;
  }

  public void setMovieId (Long movieId) {
    this.movieId = movieId;
  }

  public Integer getRating () {
    return rating;
  }

  public void setRating (Integer rating) {
    this.rating = rating;
  }

  public String getContent () {
    return content;
  }

  public void setContent (String content) {
    this.content = content;
  }
}