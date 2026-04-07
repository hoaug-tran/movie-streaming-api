package com.hoaug.movieapi.modules.subscription.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SubscriptionPlan {

  private Long id;
  private String name;
  private String code;
  private String description;
  private BigDecimal price;
  private Integer durationDays;
  private Integer maxDevices;
  private String videoQuality;
  private Boolean hasAdsFree;
  private Boolean isActive;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public String getName () {
    return name;
  }

  public void setName (String name) {
    this.name = name;
  }

  public String getCode () {
    return code;
  }

  public void setCode (String code) {
    this.code = code;
  }

  public String getDescription () {
    return description;
  }

  public void setDescription (String description) {
    this.description = description;
  }

  public BigDecimal getPrice () {
    return price;
  }

  public void setPrice (BigDecimal price) {
    this.price = price;
  }

  public Integer getDurationDays () {
    return durationDays;
  }

  public void setDurationDays (Integer durationDays) {
    this.durationDays = durationDays;
  }

  public Integer getMaxDevices () {
    return maxDevices;
  }

  public void setMaxDevices (Integer maxDevices) {
    this.maxDevices = maxDevices;
  }

  public String getVideoQuality () {
    return videoQuality;
  }

  public void setVideoQuality (String videoQuality) {
    this.videoQuality = videoQuality;
  }

  public Boolean getHasAdsFree () {
    return hasAdsFree;
  }

  public void setHasAdsFree (Boolean hasAdsFree) {
    this.hasAdsFree = hasAdsFree;
  }

  public Boolean getIsActive () {
    return isActive;
  }

  public void setIsActive (Boolean active) {
    isActive = active;
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