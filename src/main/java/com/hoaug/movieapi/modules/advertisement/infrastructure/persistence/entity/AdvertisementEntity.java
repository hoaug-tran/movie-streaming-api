package com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.hoaug.movieapi.modules.advertisement.domain.model.AdvertisementType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "advertisements")
public class AdvertisementEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 255)
  private String title;

  @Column(name = "video_url", nullable = false, length = 500)
  private String videoUrl;

  @Column(name = "target_url", length = 500)
  private String targetUrl;

  @Column(name = "duration_seconds", nullable = false)
  private Integer durationSeconds;

  @Enumerated(EnumType.STRING)
  @Column(name = "ad_type", nullable = false, length = 20)
  private AdvertisementType adType;

  @Column(nullable = false)
  private Integer priority;

  @Column(name = "is_skippable", nullable = false)
  private Boolean isSkippable;

  @Column(name = "skip_after_seconds")
  private Integer skipAfterSeconds;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  @Column(name = "start_at")
  private LocalDateTime startAt;

  @Column(name = "end_at")
  private LocalDateTime endAt;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
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