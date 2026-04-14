package com.hoaug.movieapi.modules.payment.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.common.config.PayOSConfig;
import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentMethod;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentTransaction;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionPlan;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;
import com.hoaug.movieapi.modules.subscription.domain.repository.PaymentTransactionRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

  private final PaymentTransactionRepository paymentTransactionRepository;
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionPlanRepository subscriptionPlanRepository;
  private final PayOS payOS;
  private final PayOSConfig payOSConfig;

  @Transactional
  public PaymentLinkResponse createPaymentLink (Long userId, Long planId) {
    SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

    String orderCode = "ORDER_" + userId + "_" + System.currentTimeMillis();
    String numericOrderCode = orderCode.replaceAll("\\D", "");
    BigDecimal amountInVND = plan.getPrice();

    UserSubscription subscription = new UserSubscription();
    subscription.setUserId(userId);
    subscription.setPlanId(planId);
    subscription.setStartAt(LocalDateTime.now());
    subscription.setEndAt(LocalDateTime.now().plusDays(plan.getDurationDays()));
    subscription.setStatus(SubscriptionStatus.PENDING);
    subscription.setAutoRenew(false);
    UserSubscription savedSubscription = userSubscriptionRepository.save(subscription);

    PaymentTransaction transaction = new PaymentTransaction();
    transaction.setUserId(userId);
    transaction.setSubscriptionId(savedSubscription.getId());
    transaction.setAmount(amountInVND);
    transaction.setCurrency("VND");
    transaction.setPaymentMethod(PaymentMethod.VNPAY);
    transaction.setStatus(PaymentStatus.PENDING);
    transaction.setProviderTransactionId(numericOrderCode);

    PaymentTransaction savedTransaction = paymentTransactionRepository.save(transaction);
    log.info("Payment transaction created: orderId={}, userId={}, planId={}", orderCode, userId,
        planId);

    try {
      CreatePaymentLinkRequest request = CreatePaymentLinkRequest.builder()
          .orderCode(Long.parseLong(numericOrderCode)).amount(amountInVND.longValue())
          .description("Subscription: " + plan.getName()).returnUrl(payOSConfig.getReturnUrl())
          .cancelUrl(payOSConfig.getCancelUrl()).build();

      var response = payOS.paymentRequests().create(request);

      return PaymentLinkResponse.builder().paymentId(savedTransaction.getId()).orderCode(orderCode)
          .checkoutUrl(response.getCheckoutUrl()).amount(amountInVND.longValue())
          .planName(plan.getName()).build();

    } catch (Exception e) {
      log.error("PayOS error creating payment link: {}", e.getMessage(), e);
      throw new AppException(ErrorCode.PAYMENT_CREATION_FAILED);
    }
  }

  @Transactional
  public void completePayment (String orderCode, String transactionId, String providerResponse) {
    PaymentTransaction transaction = paymentTransactionRepository
        .findByProviderTransactionId(orderCode)
        .orElseThrow( () -> new AppException(ErrorCode.PAYMENT_TRANSACTION_NOT_FOUND));

    transaction.setStatus(PaymentStatus.SUCCESS);
    transaction.setPaidAt(LocalDateTime.now());
    transaction.setProviderResponse(providerResponse);
    paymentTransactionRepository.save(transaction);

    UserSubscription subscription = userSubscriptionRepository
        .findById(transaction.getSubscriptionId())
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

    subscription.setStatus(SubscriptionStatus.ACTIVE);
    userSubscriptionRepository.save(subscription);

    log.info("Payment completed successfully: orderId={}, transactionId={}", orderCode,
        transactionId);
  }

  @Transactional
  public void failPayment (String orderCode, String failureReason) {
    PaymentTransaction transaction = paymentTransactionRepository
        .findByProviderTransactionId(orderCode)
        .orElseThrow( () -> new AppException(ErrorCode.PAYMENT_TRANSACTION_NOT_FOUND));

    transaction.setStatus(PaymentStatus.FAILED);
    transaction.setProviderResponse(failureReason);
    paymentTransactionRepository.save(transaction);

    UserSubscription subscription = userSubscriptionRepository
        .findById(transaction.getSubscriptionId())
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

    subscription.setStatus(SubscriptionStatus.PENDING);
    userSubscriptionRepository.save(subscription);

    log.warn("Payment failed: orderId={}, reason={}", orderCode, failureReason);
  }

  public PaymentSuccessResponse getPaymentByOrderCode (String orderCode) {
    PaymentTransaction transaction = paymentTransactionRepository
        .findByProviderTransactionId(orderCode)
        .orElseThrow( () -> new AppException(ErrorCode.PAYMENT_TRANSACTION_NOT_FOUND));

    UserSubscription subscription = userSubscriptionRepository
        .findById(transaction.getSubscriptionId())
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

    SubscriptionPlan plan = subscriptionPlanRepository.findById(subscription.getPlanId())
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

    return PaymentSuccessResponse.builder().paymentId(transaction.getId()).orderCode(orderCode)
        .amount(transaction.getAmount().longValue()).status(transaction.getStatus().toString())
        .paidAt(transaction.getPaidAt()).subscriptionId(subscription.getId())
        .planName(plan.getName()).planDuration(plan.getDurationDays())
        .subscriptionStatus(subscription.getStatus().toString()).startAt(subscription.getStartAt())
        .endAt(subscription.getEndAt()).build();
  }

  public static class PaymentSuccessResponse {
    private Long paymentId;
    private String orderCode;
    private Long amount;
    private String status;
    private LocalDateTime paidAt;
    private Long subscriptionId;
    private String planName;
    private Integer planDuration;
    private String subscriptionStatus;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public PaymentSuccessResponse(Long paymentId, String orderCode, Long amount, String status,
        LocalDateTime paidAt, Long subscriptionId, String planName, Integer planDuration,
        String subscriptionStatus, LocalDateTime startAt, LocalDateTime endAt) {
      this.paymentId = paymentId;
      this.orderCode = orderCode;
      this.amount = amount;
      this.status = status;
      this.paidAt = paidAt;
      this.subscriptionId = subscriptionId;
      this.planName = planName;
      this.planDuration = planDuration;
      this.subscriptionStatus = subscriptionStatus;
      this.startAt = startAt;
      this.endAt = endAt;
    }

    public static Builder builder () {
      return new Builder();
    }

    public static class Builder {
      private Long paymentId;
      private String orderCode;
      private Long amount;
      private String status;
      private LocalDateTime paidAt;
      private Long subscriptionId;
      private String planName;
      private Integer planDuration;
      private String subscriptionStatus;
      private LocalDateTime startAt;
      private LocalDateTime endAt;

      public Builder paymentId (Long paymentId) {
        this.paymentId = paymentId;
        return this;
      }

      public Builder orderCode (String orderCode) {
        this.orderCode = orderCode;
        return this;
      }

      public Builder amount (Long amount) {
        this.amount = amount;
        return this;
      }

      public Builder status (String status) {
        this.status = status;
        return this;
      }

      public Builder paidAt (LocalDateTime paidAt) {
        this.paidAt = paidAt;
        return this;
      }

      public Builder subscriptionId (Long subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
      }

      public Builder planName (String planName) {
        this.planName = planName;
        return this;
      }

      public Builder planDuration (Integer planDuration) {
        this.planDuration = planDuration;
        return this;
      }

      public Builder subscriptionStatus (String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
        return this;
      }

      public Builder startAt (LocalDateTime startAt) {
        this.startAt = startAt;
        return this;
      }

      public Builder endAt (LocalDateTime endAt) {
        this.endAt = endAt;
        return this;
      }

      public PaymentSuccessResponse build () {
        return new PaymentSuccessResponse(paymentId, orderCode, amount, status, paidAt,
            subscriptionId, planName, planDuration, subscriptionStatus, startAt, endAt);
      }
    }

    public Long getPaymentId () {
      return paymentId;
    }

    public String getOrderCode () {
      return orderCode;
    }

    public Long getAmount () {
      return amount;
    }

    public String getStatus () {
      return status;
    }

    public LocalDateTime getPaidAt () {
      return paidAt;
    }

    public Long getSubscriptionId () {
      return subscriptionId;
    }

    public String getPlanName () {
      return planName;
    }

    public Integer getPlanDuration () {
      return planDuration;
    }

    public String getSubscriptionStatus () {
      return subscriptionStatus;
    }

    public LocalDateTime getStartAt () {
      return startAt;
    }

    public LocalDateTime getEndAt () {
      return endAt;
    }
  }

  public static class PaymentLinkResponse {
    private Long paymentId;
    private String orderCode;
    private String checkoutUrl;
    private Long amount;
    private String planName;

    public PaymentLinkResponse(Long paymentId, String orderCode, String checkoutUrl, Long amount,
        String planName) {
      this.paymentId = paymentId;
      this.orderCode = orderCode;
      this.checkoutUrl = checkoutUrl;
      this.amount = amount;
      this.planName = planName;
    }

    public static Builder builder () {
      return new Builder();
    }

    public static class Builder {
      private Long paymentId;
      private String orderCode;
      private String checkoutUrl;
      private Long amount;
      private String planName;

      public Builder paymentId (Long paymentId) {
        this.paymentId = paymentId;
        return this;
      }

      public Builder orderCode (String orderCode) {
        this.orderCode = orderCode;
        return this;
      }

      public Builder checkoutUrl (String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
        return this;
      }

      public Builder amount (Long amount) {
        this.amount = amount;
        return this;
      }

      public Builder planName (String planName) {
        this.planName = planName;
        return this;
      }

      public PaymentLinkResponse build () {
        return new PaymentLinkResponse(paymentId, orderCode, checkoutUrl, amount, planName);
      }
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

    public Long getAmount () {
      return amount;
    }

    public String getPlanName () {
      return planName;
    }
  }
}
