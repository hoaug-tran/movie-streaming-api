package com.hoaug.movieapi.modules.subscription.application.dto.response;

import java.time.LocalDateTime;

public class UserSubscriptionResponse {

  private Long id;
  private Long userId;
  private Long planId;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
  private String status;
  private Boolean autoRenew;
  private SubscriptionPlanResponse plan;
  private Long remainingSeconds;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public Long getUserId () {
    return userId;
  }

  public void setUserId (Long userId) {
    this.userId = userId;
  }

  public Long getPlanId () {
    return planId;
  }

  public void setPlanId (Long planId) {
    this.planId = planId;
  }

  public LocalDateTime getStartAt () {
    return startAt;
  }

  public void setStartAt (LocalDateTime startAt) {
    this.startAt = startAt;
  }

  public LocalDateTime getEndAt () {
    return endAt;
  }

  public void setEndAt (LocalDateTime endAt) {
    this.endAt = endAt;
  }

  public String getStatus () {
    return status;
  }

  public void setStatus (String status) {
    this.status = status;
  }

  public Boolean getAutoRenew () {
    return autoRenew;
  }

  public void setAutoRenew (Boolean autoRenew) {
    this.autoRenew = autoRenew;
  }

  public SubscriptionPlanResponse getPlan () {
    return plan;
  }

  public void setPlan (SubscriptionPlanResponse plan) {
    this.plan = plan;
  }

  public Long getRemainingSeconds () {
    return remainingSeconds;
  }

  public void setRemainingSeconds (Long remainingSeconds) {
    this.remainingSeconds = remainingSeconds;
  }

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt () {
    return updatedAt;
  }

  public void setUpdatedAt (LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}