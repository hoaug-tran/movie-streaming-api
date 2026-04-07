package com.hoaug.movieapi.common.event;

public class SubscriptionActivatedEvent extends DomainEvent {
  private final Long userId;
  private final Long subscriptionPlanId;

  public SubscriptionActivatedEvent(Long userId, Long subscriptionPlanId) {
    this.userId = userId;
    this.subscriptionPlanId = subscriptionPlanId;
  }

  public Long getUserId () {
    return userId;
  }

  public Long getSubscriptionPlanId () {
    return subscriptionPlanId;
  }
}
