package com.hoaug.movieapi.modules.watchhistory.domain.model;

import java.time.LocalDateTime;

public class WatchHistory {
  private Long id;
  private Long userId;
  private Long movieId;
  private Long episodeId;
  private Integer watchedDurationSeconds;
  private Integer stoppedAtSecond;
  private Boolean isCompleted;
  private LocalDateTime lastWatchedAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

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

  public Long getEpisodeId () {
    return episodeId;
  }

  public void setEpisodeId (Long episodeId) {
    this.episodeId = episodeId;
  }

  public Integer getWatchedDurationSeconds () {
    return watchedDurationSeconds;
  }

  public void setWatchedDurationSeconds (Integer watchedDurationSeconds) {
    this.watchedDurationSeconds = watchedDurationSeconds;
  }

  public Integer getStoppedAtSecond () {
    return stoppedAtSecond;
  }

  public void setStoppedAtSecond (Integer stoppedAtSecond) {
    this.stoppedAtSecond = stoppedAtSecond;
  }

  public Boolean getIsCompleted () {
    return isCompleted;
  }

  public void setIsCompleted (Boolean completed) {
    isCompleted = completed;
  }

  public LocalDateTime getLastWatchedAt () {
    return lastWatchedAt;
  }

  public void setLastWatchedAt (LocalDateTime lastWatchedAt) {
    this.lastWatchedAt = lastWatchedAt;
  }

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt () {
    return updatedAt;
  }

  public void setUpdatedAt (LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}