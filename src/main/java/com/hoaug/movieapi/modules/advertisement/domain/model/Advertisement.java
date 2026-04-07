package com.hoaug.movieapi.modules.advertisement.domain.model;

import java.time.LocalDateTime;

public class Advertisement {

  private Long id;
  private String title;
  private String videoUrl;
  private String targetUrl;
  private Integer durationSeconds;
  private AdvertisementType adType;
  private Integer priority;
  private Boolean isSkippable;
  private Integer skipAfterSeconds;
  private Boolean isActive;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

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

  public String getVideoUrl () {
    return videoUrl;
  }

  public void setVideoUrl (String videoUrl) {
    this.videoUrl = videoUrl;
  }

  public String getTargetUrl () {
    return targetUrl;
  }

  public void setTargetUrl (String targetUrl) {
    this.targetUrl = targetUrl;
  }

  public Integer getDurationSeconds () {
    return durationSeconds;
  }

  public void setDurationSeconds (Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
  }

  public AdvertisementType getAdType () {
    return adType;
  }

  public void setAdType (AdvertisementType adType) {
    this.adType = adType;
  }

  public Integer getPriority () {
    return priority;
  }

  public void setPriority (Integer priority) {
    this.priority = priority;
  }

  public Boolean getIsSkippable () {
    return isSkippable;
  }

  public void setIsSkippable (Boolean skippable) {
    isSkippable = skippable;
  }

  public Integer getSkipAfterSeconds () {
    return skipAfterSeconds;
  }

  public void setSkipAfterSeconds (Integer skipAfterSeconds) {
    this.skipAfterSeconds = skipAfterSeconds;
  }

  public Boolean getIsActive () {
    return isActive;
  }

  public void setIsActive (Boolean active) {
    isActive = active;
  }

  public LocalDateTime getStartAt () {
    return startAt;
  }

  public void setStartAt (LocalDateTime startAt) {
    this.startAt = startAt;
  }

  public LocalDateTime getEndAt () {
    return endAt;
  }

  public void setEndAt (LocalDateTime endAt) {
    this.endAt = endAt;
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