package com.hoaug.movieapi.modules.watchlist.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.hoaug.movieapi.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "watchlists")
public class WatchlistEntity extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "movie_id", nullable = false)
  private Long movieId;

  @Column(name = "added_at", nullable = false)
  private LocalDateTime addedAt;

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