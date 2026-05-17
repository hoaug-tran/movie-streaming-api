package com.hoaug.movieapi.modules.movie.domain.model;

import java.time.LocalDateTime;

public class Episode {
  private Long id;
  private Long movieId;
  private String title;
  private Integer episodeNumber;
  private String videoUrl;
  private String availableQualities;
  private String thumbnailUrl;
  private Integer durationSeconds;
  private Boolean isFreePreview;
  private EpisodeStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public Long getMovieId () {
    return movieId;
  }

  public void setMovieId (Long movieId) {
    this.movieId = movieId;
  }

  public String getTitle () {
    return title;
  }

  public void setTitle (String title) {
    this.title = title;
  }

  public Integer getEpisodeNumber () {
    return episodeNumber;
  }

  public void setEpisodeNumber (Integer episodeNumber) {
    this.episodeNumber = episodeNumber;
  }

  public String getVideoUrl () {
    return videoUrl;
  }

  public void setVideoUrl (String videoUrl) {
    this.videoUrl = videoUrl;
  }

  public String getAvailableQualities () {
    return availableQualities;
  }

  public void setAvailableQualities (String availableQualities) {
    this.availableQualities = availableQualities;
  }

  public String getThumbnailUrl () {
    return thumbnailUrl;
  }

  public void setThumbnailUrl (String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }

  public Integer getDurationSeconds () {
    return durationSeconds;
  }

  public void setDurationSeconds (Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
  }

  public Boolean getIsFreePreview () {
    return isFreePreview;
  }

  public void setIsFreePreview (Boolean isFreePreview) {
    this.isFreePreview = isFreePreview;
  }

  public EpisodeStatus getStatus () {
    return status;
  }

  public void setStatus (EpisodeStatus status) {
    this.status = status;
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
