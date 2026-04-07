package com.hoaug.movieapi.modules.subscription.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentTransactionResponse {

  private Long id;
  private Long subscriptionId;
  private BigDecimal amount;
  private String currency;
  private String paymentMethod;
  private String status;
  private String providerTransactionId;
  private LocalDateTime paidAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public Long getSubscriptionId () {
    return subscriptionId;
  }

  public void setSubscriptionId (Long subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public BigDecimal getAmount () {
    return amount;
  }

  public void setAmount (BigDecimal amount) {
    this.amount = amount;
  }

  public String getCurrency () {
    return currency;
  }

  public void setCurrency (String currency) {
    this.currency = currency;
  }

  public String getPaymentMethod () {
    return paymentMethod;
  }

  public void setPaymentMethod (String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public String getStatus () {
    return status;
  }

  public void setStatus (String status) {
    this.status = status;
  }

  public String getProviderTransactionId () {
    return providerTransactionId;
  }

  public void setProviderTransactionId (String providerTransactionId) {
    this.providerTransactionId = providerTransactionId;
  }

  public LocalDateTime getPaidAt () {
    return paidAt;
  }

  public void setPaidAt (LocalDateTime paidAt) {
    this.paidAt = paidAt;
  }
}