package com.hoaug.movieapi.modules.subscription.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentTransaction {

  private Long id;
  private Long userId;
  private Long subscriptionId;
  private BigDecimal amount;
  private String currency;
  private PaymentMethod paymentMethod;
  private PaymentStatus status;
  private String providerTransactionId;
  private String providerResponse;
  private LocalDateTime paidAt;
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

  public PaymentMethod getPaymentMethod () {
    return paymentMethod;
  }

  public void setPaymentMethod (PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public PaymentStatus getStatus () {
    return status;
  }

  public void setStatus (PaymentStatus status) {
    this.status = status;
  }

  public String getProviderTransactionId () {
    return providerTransactionId;
  }

  public void setProviderTransactionId (String providerTransactionId) {
    this.providerTransactionId = providerTransactionId;
  }

  public String getProviderResponse () {
    return providerResponse;
  }

  public void setProviderResponse (String providerResponse) {
    this.providerResponse = providerResponse;
  }

  public LocalDateTime getPaidAt () {
    return paidAt;
  }

  public void setPaidAt (LocalDateTime paidAt) {
    this.paidAt = paidAt;
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