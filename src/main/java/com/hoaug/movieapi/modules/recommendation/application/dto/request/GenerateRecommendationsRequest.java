package com.hoaug.movieapi.modules.recommendation.application.dto.request;

import jakarta.validation.constraints.NotNull;

public class GenerateRecommendationsRequest {

  @NotNull
  private Long userId;

  private Integer limit;

  public Long getUserId () {
    return userId;
  }

  public void setUserId (Long userId) {
    this.userId = userId;
  }

  public Integer getLimit () {
    return limit;
  }

  public void setLimit (Integer limit) {
    this.limit = limit;
  }
}