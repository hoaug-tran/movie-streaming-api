package com.hoaug.movieapi.modules.subscription.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.subscription.application.dto.response.InvoiceResponse;
import com.hoaug.movieapi.modules.subscription.application.dto.response.PaymentTransactionResponse;
import com.hoaug.movieapi.modules.subscription.application.dto.response.SubscriptionPlanResponse;
import com.hoaug.movieapi.modules.subscription.application.dto.response.UserSubscriptionResponse;
import com.hoaug.movieapi.modules.subscription.domain.model.Invoice;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentTransaction;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionPlan;
import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;

@Component
public class SubscriptionMapper {

  public SubscriptionPlanResponse toResponse (SubscriptionPlan plan) {
    SubscriptionPlanResponse response = new SubscriptionPlanResponse();
    response.setId(plan.getId());
    response.setName(plan.getName());
    response.setCode(plan.getCode());
    response.setDescription(plan.getDescription());
    response.setPrice(plan.getPrice());
    response.setDurationDays(plan.getDurationDays());
    response.setMaxDevices(plan.getMaxDevices());
    response.setVideoQuality(plan.getVideoQuality());
    response.setHasAdsFree(plan.getHasAdsFree());
    response.setIsActive(plan.getIsActive());
    return response;
  }

  public UserSubscriptionResponse toResponse (UserSubscription subscription) {
    UserSubscriptionResponse response = new UserSubscriptionResponse();
    response.setId(subscription.getId());
    response.setUserId(subscription.getUserId());
    response.setPlanId(subscription.getPlanId());
    response.setStartAt(subscription.getStartAt());
    response.setEndAt(subscription.getEndAt());
    response.setStatus(subscription.getStatus().name());
    response.setAutoRenew(subscription.getAutoRenew());
    response.setCreatedAt(subscription.getCreatedAt());
    response.setUpdatedAt(subscription.getUpdatedAt());
    return response;
  }

  public PaymentTransactionResponse toResponse (PaymentTransaction transaction) {
    PaymentTransactionResponse response = new PaymentTransactionResponse();
    response.setId(transaction.getId());
    response.setSubscriptionId(transaction.getSubscriptionId());
    response.setAmount(transaction.getAmount());
    response.setCurrency(transaction.getCurrency());
    response.setPaymentMethod(transaction.getPaymentMethod().name());
    response.setStatus(transaction.getStatus().name());
    response.setProviderTransactionId(transaction.getProviderTransactionId());
    response.setPaidAt(transaction.getPaidAt());
    return response;
  }

  public InvoiceResponse toResponse (Invoice invoice) {
    InvoiceResponse response = new InvoiceResponse();
    response.setId(invoice.getId());
    response.setPaymentTransactionId(invoice.getPaymentTransactionId());
    response.setInvoiceNumber(invoice.getInvoiceNumber());
    response.setBuyerName(invoice.getBuyerName());
    response.setBuyerEmail(invoice.getBuyerEmail());
    response.setAmount(invoice.getAmount());
    response.setIssuedAt(invoice.getIssuedAt());
    return response;
  }
}