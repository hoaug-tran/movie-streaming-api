package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hoaug.movieapi.modules.subscription.domain.model.PaymentMethod;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_transactions")
public class PaymentTransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "subscription_id")
  private Long subscriptionId;

  private BigDecimal amount;
  private String currency;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method")
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  private PaymentStatus status;

  @Column(name = "provider_transaction_id")
  private String providerTransactionId;

  @Column(name = "provider_response", columnDefinition = "TEXT")
  private String providerResponse;

  @Column(name = "paid_at")
  private LocalDateTime paidAt;

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