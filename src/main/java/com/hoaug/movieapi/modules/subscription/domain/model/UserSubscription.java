package com.hoaug.movieapi.modules.subscription.domain.model;

import java.time.LocalDateTime;

public class UserSubscription {

  private Long id;
  private Long userId;
  private Long planId;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
  private SubscriptionStatus status;
  private Boolean autoRenew;
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

  public SubscriptionStatus getStatus () {
    return status;
  }

  public void setStatus (SubscriptionStatus status) {
    this.status = status;
  }

  public Boolean getAutoRenew () {
    return autoRenew;
  }

  public void setAutoRenew (Boolean autoRenew) {
    this.autoRenew = autoRenew;
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