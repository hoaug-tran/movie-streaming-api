package com.hoaug.movieapi.modules.advertisement.application.dto.request;

import jakarta.validation.constraints.NotNull;

public class MarkAdvertisementClickedRequest {

  @NotNull
  private Long advertisementViewId;

  public Long getAdvertisementViewId () {
    return advertisementViewId;
  }

  public void setAdvertisementViewId (Long advertisementViewId) {
    this.advertisementViewId = advertisementViewId;
  }
}