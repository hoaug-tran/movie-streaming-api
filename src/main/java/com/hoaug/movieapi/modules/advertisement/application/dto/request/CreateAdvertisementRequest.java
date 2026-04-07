package com.hoaug.movieapi.modules.advertisement.application.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateAdvertisementRequest {

  @NotBlank
  private String title;

  @NotBlank
  private String videoUrl;

  private String targetUrl;

  @NotNull
  private Integer durationSeconds;

  @NotBlank
  private String adType;

  @NotNull
  private Integer priority;

  @NotNull
  private Boolean isSkippable;

  private Integer skipAfterSeconds;

  @NotNull
  private Boolean isActive;

  private LocalDateTime startAt;
  private LocalDateTime endAt;

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

  public String getAdType () {
    return adType;
  }

  public void setAdType (String adType) {
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
}