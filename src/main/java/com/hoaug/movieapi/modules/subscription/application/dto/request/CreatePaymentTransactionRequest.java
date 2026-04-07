package com.hoaug.movieapi.modules.subscription.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreatePaymentTransactionRequest {

  @NotNull
  private Long subscriptionId;

  @NotBlank
  private String paymentMethod;

  private String currency;

  public Long getSubscriptionId () {
    return subscriptionId;
  }

  public void setSubscriptionId (Long subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public String getPaymentMethod () {
    return paymentMethod;
  }

  public void setPaymentMethod (String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public String getCurrency () {
    return currency;
  }

  public void setCurrency (String currency) {
    this.currency = currency;
  }
}