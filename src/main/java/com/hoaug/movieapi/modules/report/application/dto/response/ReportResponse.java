package com.hoaug.movieapi.modules.report.application.dto.response;

import java.time.LocalDateTime;

public class ReportResponse {

  private Long id;
  private Long reporterUserId;
  private Long commentId;
  private Long reviewId;
  private String reason;
  private String description;
  private String status;
  private LocalDateTime createdAt;
  private LocalDateTime resolvedAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public Long getReporterUserId () {
    return reporterUserId;
  }

  public void setReporterUserId (Long reporterUserId) {
    this.reporterUserId = reporterUserId;
  }

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

  public String getStatus () {
    return status;
  }

  public void setStatus (String status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getResolvedAt () {
    return resolvedAt;
  }

  public void setResolvedAt (LocalDateTime resolvedAt) {
    this.resolvedAt = resolvedAt;
  }
}