package com.hoaug.movieapi.modules.subscription.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.subscription.application.dto.request.CreatePaymentTransactionRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.response.PaymentTransactionResponse;
import com.hoaug.movieapi.modules.subscription.application.mapper.SubscriptionMapper;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentMethod;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentTransaction;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionPlan;
import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;
import com.hoaug.movieapi.modules.subscription.domain.repository.PaymentTransactionRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;

@Component
public class CreatePaymentTransactionUseCase {

  private final PaymentTransactionRepository paymentTransactionRepository;
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionPlanRepository subscriptionPlanRepository;
  private final SubscriptionMapper subscriptionMapper;

  public CreatePaymentTransactionUseCase(PaymentTransactionRepository paymentTransactionRepository,
      UserSubscriptionRepository userSubscriptionRepository,
      SubscriptionPlanRepository subscriptionPlanRepository,
      SubscriptionMapper subscriptionMapper) {
    this.paymentTransactionRepository = paymentTransactionRepository;
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.subscriptionPlanRepository = subscriptionPlanRepository;
    this.subscriptionMapper = subscriptionMapper;
  }

  public PaymentTransactionResponse execute (Long userId, CreatePaymentTransactionRequest request) {
    UserSubscription userSubscription = userSubscriptionRepository
        .findById(request.getSubscriptionId())
        .orElseThrow( () -> new AppException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND));

    if (!userSubscription.getUserId().equals(userId)) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    SubscriptionPlan plan = subscriptionPlanRepository.findById(userSubscription.getPlanId())
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

    PaymentTransaction transaction = new PaymentTransaction();
    transaction.setUserId(userId);
    transaction.setSubscriptionId(userSubscription.getId());
    transaction.setAmount(plan.getPrice());
    transaction.setCurrency(request.getCurrency() == null ? "VND" : request.getCurrency());
    transaction.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
    transaction.setStatus(PaymentStatus.PENDING);
    transaction.setCreatedAt(LocalDateTime.now());
    transaction.setUpdatedAt(LocalDateTime.now());

    return subscriptionMapper.toResponse(paymentTransactionRepository.save(transaction));
  }
}