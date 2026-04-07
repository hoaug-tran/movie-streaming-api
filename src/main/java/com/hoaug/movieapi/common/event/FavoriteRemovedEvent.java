package com.hoaug.movieapi.common.event;

public class FavoriteRemovedEvent extends DomainEvent {
  private final Long userId;
  private final Long movieId;

  public FavoriteRemovedEvent(Long userId, Long movieId) {
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
