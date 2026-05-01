package com.hoaug.movieapi.modules.subscription.application.dto.request;

import jakarta.validation.constraints.NotNull;

public class UpdateAutoRenewRequest {
  @NotNull(message = "Auto renew value is required")
  private Boolean autoRenew;

  public Boolean getAutoRenew () {
    return autoRenew;
  }

  public void setAutoRenew (Boolean autoRenew) {
    this.autoRenew = autoRenew;
  }
}
