package com.hoaug.movieapi.modules.watchhistory.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpsertWatchHistoryRequest {

  @NotNull
  private Long movieId;

  @NotNull
  private Long episodeId;

  @NotNull
  @Min(0)
  private Integer watchedDurationSeconds;

  @NotNull
  @Min(0)
  private Integer stoppedAtSecond;

  @NotNull
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