package com.hoaug.movieapi.modules.report.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.hoaug.movieapi.common.model.BaseEntity;
import com.hoaug.movieapi.modules.report.domain.model.ReportStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "reports")
public class ReportEntity extends BaseEntity {

  @Column(name = "reporter_user_id", nullable = false)
  private Long reporterUserId;

  @Column(name = "comment_id")
  private Long commentId;

  @Column(name = "review_id")
  private Long reviewId;

  @Column(nullable = false, length = 255)
  private String reason;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ReportStatus status;

  @Column(name = "resolved_at")
  private LocalDateTime resolvedAt;

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

  public ReportStatus getStatus () {
    return status;
  }

  public void setStatus (ReportStatus status) {
    this.status = status;
  }

  public LocalDateTime getResolvedAt () {
    return resolvedAt;
  }

  public void setResolvedAt (LocalDateTime resolvedAt) {
    this.resolvedAt = resolvedAt;
  }
}