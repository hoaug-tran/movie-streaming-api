package com.hoaug.movieapi.modules.report.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateReportRequest {

  private Long commentId;
  private Long reviewId;

  @NotBlank
  private String reason;

  private String description;

  public Long getCommentId () {
    return commentId;
  }

  public void setCommentId (Long commentId) {
    this.commentId = commentId;
  }

  public Long getReviewId () {
    return reviewId;
  }

  public void setReviewId (Long reviewId) {
    this.reviewId = reviewId;
  }

  public String getReason () {
    return reason;
  }

  public void setReason (String reason) {
    this.reason = reason;
  }

  public String getDescription () {
    return description;
  }

  public void setDescription (String description) {
    this.description = description;
  }
}