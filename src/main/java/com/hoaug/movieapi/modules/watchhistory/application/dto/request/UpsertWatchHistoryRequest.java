package com.hoaug.movieapi.modules.watchhistory.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpsertWatchHistoryRequest {

  @NotNull(message = "Vui lòng chọn phim.")
  @Positive(message = "ID phim phải là số dương.")
  private Long movieId;

  @NotNull(message = "Vui lòng chọn tập phim.")
  @Positive(message = "ID tập phim phải là số dương.")
  private Long episodeId;

  @NotNull(message = "Vui lòng cung cấp thời lượng đã xem.")
  @Min(value = 0, message = "Thời lượng đã xem phải lớn hơn hoặc bằng 0.")
  private Integer watchedDurationSeconds;

  @NotNull(message = "Vui lòng cung cấp vị trí dừng xem.")
  @Min(value = 0, message = "Vị trí dừng xem phải lớn hơn hoặc bằng 0.")
  private Integer stoppedAtSecond;

  @NotNull(message = "Vui lòng cho biết đã xem xong hay chưa.")
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