package com.hoaug.movieapi.modules.watchhistory.application.dto.response;

import java.time.LocalDateTime;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieBasicResponse;

public class ContinueWatchingResponse {
  private Long movieId;
  private Long episodeId;
  private String episodeTitle;
  private Integer episodeNumber;
  private Integer episodeDurationSeconds;
  private Integer stoppedAtSecond;
  private Integer watchedDurationSeconds;
  private Integer resumeSecond;
  private Double progressPercent;
  private LocalDateTime lastWatchedAt;
  private MovieBasicResponse movie;

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

  public String getEpisodeTitle () {
    return episodeTitle;
  }

  public void setEpisodeTitle (String episodeTitle) {
    this.episodeTitle = episodeTitle;
  }

  public Integer getEpisodeNumber () {
    return episodeNumber;
  }

  public void setEpisodeNumber (Integer episodeNumber) {
    this.episodeNumber = episodeNumber;
  }

  public Integer getEpisodeDurationSeconds () {
    return episodeDurationSeconds;
  }

  public void setEpisodeDurationSeconds (Integer episodeDurationSeconds) {
    this.episodeDurationSeconds = episodeDurationSeconds;
  }

  public Integer getStoppedAtSecond () {
    return stoppedAtSecond;
  }

  public void setStoppedAtSecond (Integer stoppedAtSecond) {
    this.stoppedAtSecond = stoppedAtSecond;
  }

  public Integer getWatchedDurationSeconds () {
    return watchedDurationSeconds;
  }

  public void setWatchedDurationSeconds (Integer watchedDurationSeconds) {
    this.watchedDurationSeconds = watchedDurationSeconds;
  }

  public Integer getResumeSecond () {
    return resumeSecond;
  }

  public void setResumeSecond (Integer resumeSecond) {
    this.resumeSecond = resumeSecond;
  }

  public Double getProgressPercent () {
    return progressPercent;
  }

  public void setProgressPercent (Double progressPercent) {
    this.progressPercent = progressPercent;
  }

  public LocalDateTime getLastWatchedAt () {
    return lastWatchedAt;
  }

  public void setLastWatchedAt (LocalDateTime lastWatchedAt) {
    this.lastWatchedAt = lastWatchedAt;
  }

  public MovieBasicResponse getMovie () {
    return movie;
  }

  public void setMovie (MovieBasicResponse movie) {
    this.movie = movie;
  }
}