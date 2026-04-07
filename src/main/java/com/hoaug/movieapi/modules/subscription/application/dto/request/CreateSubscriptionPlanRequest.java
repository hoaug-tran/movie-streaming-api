package com.hoaug.movieapi.modules.subscription.application.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateSubscriptionPlanRequest {

  @NotBlank
  private String name;

  @NotBlank
  private String code;

  private String description;

  @NotNull
  private BigDecimal price;

  @NotNull
  private Integer durationDays;

  @NotNull
  private Integer maxDevices;

  private String videoQuality;

  @NotNull
  private Boolean hasAdsFree;

  @NotNull
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