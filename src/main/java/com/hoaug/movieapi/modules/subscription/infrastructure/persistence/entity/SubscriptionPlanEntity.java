package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity;

import java.math.BigDecimal;

import com.hoaug.movieapi.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlanEntity extends BaseEntity {

  private String name;
  private String code;

  @Column(columnDefinition = "TEXT")
  private String description;

  private BigDecimal price;

  @Column(name = "duration_days")
  private Integer durationDays;

  @Column(name = "max_devices")
  private Integer maxDevices;

  @Column(name = "video_quality")
  private String videoQuality;

  @Column(name = "has_ads_free")
  private Boolean hasAdsFree;

  @Column(name = "is_active")
  private Boolean isActive;

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
}