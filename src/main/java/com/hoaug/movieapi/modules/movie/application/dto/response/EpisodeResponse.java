package com.hoaug.movieapi.modules.movie.application.dto.response;

import com.hoaug.movieapi.modules.movie.domain.model.EpisodeStatus;

public class EpisodeResponse {
  private Long id;
  private String title;
  private Integer episodeNumber;
  private String videoUrl;
  private String thumbnailUrl;
  private Integer durationSeconds;
  private Boolean isFreePreview;
  private EpisodeStatus status;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
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

  public void setIsFreePreview (Boolean freePreview) {
    isFreePreview = freePreview;
  }

  public EpisodeStatus getStatus () {
    return status;
  }

  public void setStatus (EpisodeStatus status) {
    this.status = status;
  }
}