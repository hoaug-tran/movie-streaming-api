package com.hoaug.movieapi.modules.payment.application.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  private static final String BILLING_NEW = "NEW";
  private static final String BILLING_RENEWAL = "RENEWAL";
  private static final String BILLING_UPGRADE = "UPGRADE";

  private final PaymentTransactionRepository paymentTransactionRepository;
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionPlanRepository subscriptionPlanRepository;
  private final PayOS payOS;
  private final PayOSConfig payOSConfig;

  @Transactional
  public PaymentLinkResponse createPaymentLink (Long userId, Long planId) {
    SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

    LocalDateTime now = LocalDateTime.now();
    BillingSummary billing = calculateBilling(userId, plan, now);
    String orderCode = "ORDER_" + userId + "_" + System.currentTimeMillis();
    String numericOrderCode = orderCode.replaceAll("\\D", "");

    UserSubscription subscription = new UserSubscription();
    subscription.setUserId(userId);
    subscription.setPlanId(planId);
    subscription.setStartAt(now);
    subscription.setEndAt(now.plusDays(plan.getDurationDays()));
    subscription.setStatus(SubscriptionStatus.PENDING);
    subscription.setAutoRenew(false);
    UserSubscription savedSubscription = userSubscriptionRepository.save(subscription);

    PaymentTransaction transaction = new PaymentTransaction();
    transaction.setUserId(userId);
    transaction.setSubscriptionId(savedSubscription.getId());
    transaction.setAmount(BigDecimal.valueOf(billing.chargedAmount));
    transaction.setCurrency("VND");
    transaction.setPaymentMethod(PaymentMethod.VNPAY);
    transaction.setStatus(PaymentStatus.PENDING);
    transaction.setProviderTransactionId(numericOrderCode);
    transaction.setProviderResponse(billing.toSnapshot());

    PaymentTransaction savedTransaction = paymentTransactionRepository.save(transaction);
    log.info("Payment transaction created: orderId={}, userId={}, planId={}, billingType={}",
        orderCode, userId, planId, billing.billingType);

    try {
      CreatePaymentLinkRequest request = CreatePaymentLinkRequest.builder()
          .orderCode(Long.parseLong(numericOrderCode)).amount(billing.chargedAmount)
          .description("Subscription: " + plan.getName()).returnUrl(payOSConfig.getReturnUrl())
          .cancelUrl(payOSConfig.getCancelUrl()).build();

      var response = payOS.paymentRequests().create(request);

      return PaymentLinkResponse.builder().paymentId(savedTransaction.getId()).orderCode(orderCode)
          .checkoutUrl(response.getCheckoutUrl()).amount(billing.chargedAmount)
          .planName(plan.getName()).billingType(billing.billingType)
          .originalAmount(billing.originalAmount).creditAmount(billing.creditAmount)
          .chargedAmount(billing.chargedAmount).remainingDays(billing.remainingDays)
          .currentPlanName(billing.currentPlanName).newPlanName(plan.getName()).build();

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

    if (transaction.getStatus() == PaymentStatus.SUCCESS) {
      log.info("Payment already completed: orderId={}, transactionId={}", orderCode,
          transactionId);
      return;
    }

    String billingSnapshot = transaction.getProviderResponse();
    transaction.setStatus(PaymentStatus.SUCCESS);
    transaction.setPaidAt(LocalDateTime.now());
    transaction.setProviderResponse(mergeProviderResponse(billingSnapshot, providerResponse));
    paymentTransactionRepository.save(transaction);

    UserSubscription subscription = userSubscriptionRepository
        .findById(transaction.getSubscriptionId())
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

    Long previousSubscriptionId = readLongSnapshotValue(billingSnapshot, "previousSubscriptionId");
    if (previousSubscriptionId != null) {
      userSubscriptionRepository.findById(previousSubscriptionId).ifPresent(previousSubscription -> {
        previousSubscription.setStatus(SubscriptionStatus.EXPIRED);
        previousSubscription.setEndAt(LocalDateTime.now());
        userSubscriptionRepository.save(previousSubscription);
      });
    }

    subscription.setStartAt(LocalDateTime.now());
    SubscriptionPlan plan = subscriptionPlanRepository.findById(subscription.getPlanId())
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));
    subscription.setEndAt(LocalDateTime.now().plusDays(plan.getDurationDays()));
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

  private BillingSummary calculateBilling (Long userId, SubscriptionPlan newPlan, LocalDateTime now) {
    BillingSummary billing = new BillingSummary();
    billing.billingType = BILLING_NEW;
    billing.originalAmount = toVnd(newPlan.getPrice());
    billing.chargedAmount = billing.originalAmount;
    billing.newPlanId = newPlan.getId();

    userSubscriptionRepository
        .findFirstByUserIdAndStatusOrderByEndAtDesc(userId, SubscriptionStatus.ACTIVE)
        .filter(subscription -> subscription.getEndAt() != null && subscription.getEndAt().isAfter(now))
        .ifPresent(activeSubscription -> applyActiveSubscriptionBilling(billing, activeSubscription,
            newPlan, now));

    return billing;
  }

  private void applyActiveSubscriptionBilling (BillingSummary billing,
      UserSubscription activeSubscription, SubscriptionPlan newPlan, LocalDateTime now) {
    SubscriptionPlan currentPlan = subscriptionPlanRepository.findById(activeSubscription.getPlanId())
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

    billing.previousSubscriptionId = activeSubscription.getId();
    billing.previousPlanId = currentPlan.getId();
    billing.currentPlanName = currentPlan.getName();
    billing.remainingDays = calculateRemainingDays(activeSubscription.getEndAt(), now);

    if (newPlan.getId().equals(currentPlan.getId())) {
      billing.billingType = BILLING_RENEWAL;
      billing.previousSubscriptionId = null;
      return;
    }

    if (newPlan.getPrice().compareTo(currentPlan.getPrice()) <= 0) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    billing.billingType = BILLING_UPGRADE;
    billing.creditAmount = calculateCredit(currentPlan, activeSubscription.getEndAt(), now);
    billing.chargedAmount = Math.max(0L, billing.originalAmount - billing.creditAmount);
  }

  private long calculateCredit (SubscriptionPlan currentPlan, LocalDateTime endAt, LocalDateTime now) {
    if (endAt == null || !endAt.isAfter(now) || currentPlan.getDurationDays() == null
        || currentPlan.getDurationDays() <= 0) {
      return 0L;
    }

    long remainingSeconds = Math.max(0L, Duration.between(now, endAt).getSeconds());
    BigDecimal totalSeconds = BigDecimal.valueOf(currentPlan.getDurationDays()).multiply(
        BigDecimal.valueOf(24 * 60 * 60));
    return currentPlan.getPrice().multiply(BigDecimal.valueOf(remainingSeconds))
        .divide(totalSeconds, 0, RoundingMode.DOWN).longValue();
  }

  private int calculateRemainingDays (LocalDateTime endAt, LocalDateTime now) {
    if (endAt == null || !endAt.isAfter(now)) {
      return 0;
    }
    long remainingSeconds = Duration.between(now, endAt).getSeconds();
    return (int) Math.ceil(remainingSeconds / (double) (24 * 60 * 60));
  }

  private long toVnd (BigDecimal amount) {
    return amount.setScale(0, RoundingMode.HALF_UP).longValue();
  }

  private Long readLongSnapshotValue (String snapshot, String key) {
    if (snapshot == null || snapshot.isBlank()) {
      return null;
    }
    Matcher matcher = Pattern.compile("\\\"" + key + "\\\":(\\d+)").matcher(snapshot);
    return matcher.find() ? Long.parseLong(matcher.group(1)) : null;
  }

  private String mergeProviderResponse (String billingSnapshot, String providerResponse) {
    if (billingSnapshot == null || billingSnapshot.isBlank()) {
      return providerResponse;
    }
    return billingSnapshot + "\n---PAYOS_RESPONSE---\n" + providerResponse;
  }

  private static class BillingSummary {
    private String billingType;
    private Long previousSubscriptionId;
    private Long previousPlanId;
    private Long newPlanId;
    private Long originalAmount = 0L;
    private Long creditAmount = 0L;
    private Long chargedAmount = 0L;
    private Integer remainingDays = 0;
    private String currentPlanName;

    private String toSnapshot () {
      return "{\"billingType\":\"" + billingType + "\",\"previousSubscriptionId\":"
          + previousSubscriptionId + ",\"previousPlanId\":" + previousPlanId + ",\"newPlanId\":"
          + newPlanId + ",\"originalAmount\":" + originalAmount + ",\"creditAmount\":"
          + creditAmount + ",\"chargedAmount\":" + chargedAmount + ",\"remainingDays\":"
          + remainingDays + ",\"currentPlanName\":\"" + nullSafe(currentPlanName)
          + "\"}";
    }

    private String nullSafe (String value) {
      return value == null ? "" : value.replace("\"", "'");
    }
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
    private String billingType;
    private Long originalAmount;
    private Long creditAmount;
    private Long chargedAmount;
    private Integer remainingDays;
    private String currentPlanName;
    private String newPlanName;

    public PaymentLinkResponse(Long paymentId, String orderCode, String checkoutUrl, Long amount,
        String planName, String billingType, Long originalAmount, Long creditAmount,
        Long chargedAmount, Integer remainingDays, String currentPlanName, String newPlanName) {
      this.paymentId = paymentId;
      this.orderCode = orderCode;
      this.checkoutUrl = checkoutUrl;
      this.amount = amount;
      this.planName = planName;
      this.billingType = billingType;
      this.originalAmount = originalAmount;
      this.creditAmount = creditAmount;
      this.chargedAmount = chargedAmount;
      this.remainingDays = remainingDays;
      this.currentPlanName = currentPlanName;
      this.newPlanName = newPlanName;
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
      private String billingType;
      private Long originalAmount;
      private Long creditAmount;
      private Long chargedAmount;
      private Integer remainingDays;
      private String currentPlanName;
      private String newPlanName;

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

      public Builder billingType (String billingType) {
        this.billingType = billingType;
        return this;
      }

      public Builder originalAmount (Long originalAmount) {
        this.originalAmount = originalAmount;
        return this;
      }

      public Builder creditAmount (Long creditAmount) {
        this.creditAmount = creditAmount;
        return this;
      }

      public Builder chargedAmount (Long chargedAmount) {
        this.chargedAmount = chargedAmount;
        return this;
      }

      public Builder remainingDays (Integer remainingDays) {
        this.remainingDays = remainingDays;
        return this;
      }

      public Builder currentPlanName (String currentPlanName) {
        this.currentPlanName = currentPlanName;
        return this;
      }

      public Builder newPlanName (String newPlanName) {
        this.newPlanName = newPlanName;
        return this;
      }

      public PaymentLinkResponse build () {
        return new PaymentLinkResponse(paymentId, orderCode, checkoutUrl, amount, planName,
            billingType, originalAmount, creditAmount, chargedAmount, remainingDays,
            currentPlanName, newPlanName);
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

    public String getBillingType () {
      return billingType;
    }

    public Long getOriginalAmount () {
      return originalAmount;
    }

    public Long getCreditAmount () {
      return creditAmount;
    }

    public Long getChargedAmount () {
      return chargedAmount;
    }

    public Integer getRemainingDays () {
      return remainingDays;
    }

    public String getCurrentPlanName () {
      return currentPlanName;
    }

    public String getNewPlanName () {
      return newPlanName;
    }
  }
}
