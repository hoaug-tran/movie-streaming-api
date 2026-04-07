package com.hoaug.movieapi.modules.favorite.application.dto.response;

import java.time.LocalDateTime;

public class FavoriteResponse {
  private Long id;
  private Long movieId;
  private LocalDateTime addedAt;

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

  public LocalDateTime getAddedAt () {
    return addedAt;
  }

  public void setAddedAt (LocalDateTime addedAt) {
    this.addedAt = addedAt;
  }
}