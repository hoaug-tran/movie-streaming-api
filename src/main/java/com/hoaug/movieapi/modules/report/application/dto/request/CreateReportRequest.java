package com.hoaug.movieapi.modules.report.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateReportRequest {

  @Positive(message = "Comment ID must be positive if provided")
  private Long commentId;

  @Positive(message = "Review ID must be positive if provided")
  private Long reviewId;

  @NotBlank(message = "Report reason is required")
  @Size(min = 1, max = 255, message = "Reason must be between 1 and 255 characters")
  @ValidSafeString(minLength = 1, maxLength = 255)
  private String reason;

  @Size(max = 2000, message = "Description must be at most 2000 characters")
  @ValidSafeString(minLength = 0, maxLength = 2000)
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