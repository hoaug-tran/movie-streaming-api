package com.hoaug.movieapi.modules.watchhistory.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpsertWatchHistoryRequest {

  @NotNull(message = "Movie ID is required")
  @Positive(message = "Movie ID must be positive")
  private Long movieId;

  @NotNull(message = "Episode ID is required")
  @Positive(message = "Episode ID must be positive")
  private Long episodeId;

  @NotNull(message = "Watched duration seconds is required")
  @Min(value = 0, message = "Watched duration seconds must be at least 0")
  private Integer watchedDurationSeconds;

  @NotNull(message = "Stopped at second is required")
  @Min(value = 0, message = "Stopped at second must be at least 0")
  private Integer stoppedAtSecond;

  @NotNull(message = "Is completed flag is required")
  private Boolean isCompleted;

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
}