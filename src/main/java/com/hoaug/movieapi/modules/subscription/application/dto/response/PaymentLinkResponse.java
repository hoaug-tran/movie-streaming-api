package com.hoaug.movieapi.modules.subscription.application.dto.response;

public class PaymentLinkResponse {
  public Long paymentId;
  public String orderCode;
  public String checkoutUrl;
  public Double amount;
  public String planName;

  public PaymentLinkResponse(Long paymentId, String orderCode, String checkoutUrl, Double amount,
      String planName) {
    this.paymentId = paymentId;
    this.orderCode = orderCode;
    this.checkoutUrl = checkoutUrl;
    this.amount = amount;
    this.planName = planName;
  }

  public Long getPaymentId () {
    return paymentId;
  }

  public String getOrderCode () {
    return orderCode;
  }

  public String getCheckoutUrl () {
    return checkoutUrl;
  }

  public Double getAmount () {
    return amount;
  }

  public String getPlanName () {
    return planName;
  }
}
