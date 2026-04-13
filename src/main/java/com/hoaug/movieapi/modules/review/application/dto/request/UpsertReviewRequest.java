package com.hoaug.movieapi.modules.review.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class UpsertReviewRequest {

  @NotNull(message = "Movie ID is required")
  @Positive(message = "Movie ID must be positive")
  private Long movieId;

  @NotNull(message = "Rating is required")
  @Min(value = 1, message = "Rating must be at least 1")
  @Max(value = 5, message = "Rating must be at most 5")
  private Integer rating;

  @NotBlank(message = "Review content is required")
  @Size(min = 1, max = 5000, message = "Review content must be between 1 and 5000 characters")
  @ValidSafeString(minLength = 1, maxLength = 5000)
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