package com.hoaug.movieapi.common.event;

public class FavoriteAddedEvent extends DomainEvent {
  private final Long userId;
  private final Long movieId;

  public FavoriteAddedEvent(Long userId, Long movieId) {
    this.userId = userId;
    this.movieId = movieId;
  }

  public Long getUserId () {
    return userId;
  }

  public Long getMovieId () {
    return movieId;
  }
}
