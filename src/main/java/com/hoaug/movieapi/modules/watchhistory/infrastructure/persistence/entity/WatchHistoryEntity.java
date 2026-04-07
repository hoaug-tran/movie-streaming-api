package com.hoaug.movieapi.modules.watchhistory.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "watch_histories")
public class WatchHistoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "movie_id", nullable = false)
  private Long movieId;

  @Column(name = "episode_id", nullable = false)
  private Long episodeId;

  @Column(name = "watched_duration_seconds", nullable = false)
  private Integer watchedDurationSeconds;

  @Column(name = "stopped_at_second", nullable = false)
  private Integer stoppedAtSecond;

  @Column(name = "is_completed", nullable = false)
  private Boolean isCompleted;

  @Column(name = "last_watched_at", nullable = false)
  private LocalDateTime lastWatchedAt;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
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