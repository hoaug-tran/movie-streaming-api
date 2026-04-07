package com.hoaug.movieapi.modules.watchlist.domain.model;

import java.time.LocalDateTime;

public class Watchlist {
  private Long id;
  private Long userId;
  private Long movieId;
  private LocalDateTime addedAt;

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

  public LocalDateTime getAddedAt () {
    return addedAt;
  }

  public void setAddedAt (LocalDateTime addedAt) {
    this.addedAt = addedAt;
  }
}