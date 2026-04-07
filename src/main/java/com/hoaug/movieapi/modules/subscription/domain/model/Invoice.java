package com.hoaug.movieapi.modules.subscription.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Invoice {

  private Long id;
  private Long paymentTransactionId;
  private String invoiceNumber;
  private String buyerName;
  private String buyerEmail;
  private BigDecimal amount;
  private LocalDateTime issuedAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public Long getPaymentTransactionId () {
    return paymentTransactionId;
  }

  public void setPaymentTransactionId (Long paymentTransactionId) {
    this.paymentTransactionId = paymentTransactionId;
  }

  public String getInvoiceNumber () {
    return invoiceNumber;
  }

  public void setInvoiceNumber (String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public String getBuyerName () {
    return buyerName;
  }

  public void setBuyerName (String buyerName) {
    this.buyerName = buyerName;
  }

  public String getBuyerEmail () {
    return buyerEmail;
  }

  public void setBuyerEmail (String buyerEmail) {
    this.buyerEmail = buyerEmail;
  }

  public BigDecimal getAmount () {
    return amount;
  }

  public void setAmount (BigDecimal amount) {
    this.amount = amount;
  }

  public LocalDateTime getIssuedAt () {
    return issuedAt;
  }

  public void setIssuedAt (LocalDateTime issuedAt) {
    this.issuedAt = issuedAt;
  }
}