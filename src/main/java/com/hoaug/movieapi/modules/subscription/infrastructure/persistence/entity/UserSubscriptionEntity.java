package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_subscriptions")
public class UserSubscriptionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "plan_id")
  private Long planId;

  @Column(name = "start_at")
  private LocalDateTime startAt;

  @Column(name = "end_at")
  private LocalDateTime endAt;

  @Enumerated(EnumType.STRING)
  private SubscriptionStatus status;

  @Column(name = "auto_renew")
  private Boolean autoRenew;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
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