package com.hoaug.movieapi.modules.subscription.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateInvoiceRequest {

  @NotNull
  private Long paymentTransactionId;

  @NotBlank
  private String buyerName;

  @NotBlank
  private String buyerEmail;

  public Long getPaymentTransactionId () {
    return paymentTransactionId;
  }

  public void setPaymentTransactionId (Long paymentTransactionId) {
    this.paymentTransactionId = paymentTransactionId;
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
}