package com.hoaug.movieapi.modules.review.application.dto.response;

import java.time.LocalDateTime;

public class ReviewResponse {
  private Long id;
  private Long movieId;
  private Integer rating;
  private String content;
  private LocalDateTime createdAt;

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

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}