package com.hoaug.movieapi.modules.review.application.dto.request;

import com.hoaug.movieapi.modules.review.domain.model.ReviewStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateReviewStatusRequest {

  @NotNull(message = "Review status is required")
  private ReviewStatus status;

  public ReviewStatus getStatus () {
    return status;
  }

  public void setStatus (ReviewStatus status) {
    this.status = status;
  }
}
