package com.hoaug.movieapi.modules.review.application.dto.request;

import com.hoaug.movieapi.modules.review.domain.model.ReviewStatus;

public class UpdateReviewStatusRequest {

  private ReviewStatus status;

  public ReviewStatus getStatus () {
    return status;
  }

  public void setStatus (ReviewStatus status) {
    this.status = status;
  }
}
