package com.hoaug.movieapi.modules.subscription.application.dto.request;

import jakarta.validation.constraints.NotNull;

public class SubscribePlanRequest {

  @NotNull
  private Long planId;

  @NotNull
  private Boolean autoRenew;

  public Long getPlanId () {
    return planId;
  }

  public void setPlanId (Long planId) {
    this.planId = planId;
  }

  public Boolean getAutoRenew () {
    return autoRenew;
  }

  public void setAutoRenew (Boolean autoRenew) {
    this.autoRenew = autoRenew;
  }
}