package com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity;

import com.hoaug.movieapi.common.model.BaseEntity;
import com.hoaug.movieapi.modules.movie.domain.model.EpisodeStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "episodes")
public class EpisodeEntity extends BaseEntity {

  @Column(name = "movie_id", nullable = false)
  private Long movieId;

  @Column(nullable = false, length = 255)
  private String title;

  @Column(name = "episode_number", nullable = false)
  private Integer episodeNumber;

  @Column(name = "video_url", nullable = false, length = 500)
  private String videoUrl;

  @Column(name = "available_qualities", length = 100)
  private String availableQualities;

  @Column(name = "thumbnail_url", length = 500)
  private String thumbnailUrl;

  @Column(name = "duration_seconds", nullable = false)
  private Integer durationSeconds;

  @Column(name = "is_free_preview", nullable = false)
  private Boolean isFreePreview;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private EpisodeStatus status;

  @Column(name = "transcode_version", length = 16)
  private String transcodeVersion;

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

  public void setIsFreePreview (Boolean freePreview) {
    isFreePreview = freePreview;
  }

  public EpisodeStatus getStatus () {
    return status;
  }

  public void setStatus (EpisodeStatus status) {
    this.status = status;
  }

  public String getTranscodeVersion () {
    return transcodeVersion;
  }

  public void setTranscodeVersion (String transcodeVersion) {
    this.transcodeVersion = transcodeVersion;
  }
}