package com.hoaug.movieapi.modules.advertisement.application.dto.request;

import java.time.LocalDateTime;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateAdvertisementRequest {

  @NotBlank(message = "Advertisement title is required")
  @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
  @ValidSafeString(minLength = 1, maxLength = 255)
  private String title;

  @NotBlank(message = "Video URL is required")
  @Size(min = 1, max = 2048, message = "Video URL must be between 1 and 2048 characters")
  @ValidSafeString(minLength = 1, maxLength = 2048)
  private String videoUrl;

  @Size(max = 2048, message = "Target URL must be at most 2048 characters")
  @ValidSafeString(minLength = 0, maxLength = 2048)
  private String targetUrl;

  @NotNull(message = "Duration seconds is required")
  @Positive(message = "Duration seconds must be positive")
  private Integer durationSeconds;

  @NotBlank(message = "Ad type is required")
  @Size(min = 1, max = 50, message = "Ad type must be between 1 and 50 characters")
  @ValidSafeString(minLength = 1, maxLength = 50)
  private String adType;

  @NotNull(message = "Priority is required")
  @Positive(message = "Priority must be positive")
  private Integer priority;

  @NotNull(message = "Is skippable flag is required")
  private Boolean isSkippable;

  private Integer skipAfterSeconds;

  @NotNull(message = "Is active flag is required")
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