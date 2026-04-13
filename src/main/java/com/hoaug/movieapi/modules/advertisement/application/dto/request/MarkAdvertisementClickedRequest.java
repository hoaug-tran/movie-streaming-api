package com.hoaug.movieapi.modules.advertisement.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MarkAdvertisementClickedRequest {

  @NotNull(message = "Advertisement view ID is required")
  @Positive(message = "Advertisement view ID must be positive")
  private Long advertisementViewId;

  public Long getAdvertisementViewId () {
    return advertisementViewId;
  }

  public void setAdvertisementViewId (Long advertisementViewId) {
    this.advertisementViewId = advertisementViewId;
  }
}