package com.hoaug.movieapi.modules.subscription.application.usecase;

import java.time.LocalDateTime;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.subscription.application.dto.response.PaymentTransactionResponse;
import com.hoaug.movieapi.modules.subscription.application.mapper.SubscriptionMapper;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentTransaction;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;
import com.hoaug.movieapi.modules.subscription.domain.repository.PaymentTransactionRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;

@Component
public class MarkPaymentSuccessUseCase {

  private final PaymentTransactionRepository paymentTransactionRepository;
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionMapper subscriptionMapper;

  public MarkPaymentSuccessUseCase(PaymentTransactionRepository paymentTransactionRepository,
      UserSubscriptionRepository userSubscriptionRepository,
      SubscriptionMapper subscriptionMapper) {
    this.paymentTransactionRepository = paymentTransactionRepository;
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.subscriptionMapper = subscriptionMapper;
  }

  @CacheEvict(cacheNames = "userSubscription", allEntries = true)
  public PaymentTransactionResponse execute (Long transactionId, String providerTransactionId,
      String providerResponse) {
    PaymentTransaction transaction = paymentTransactionRepository.findById(transactionId)
        .orElseThrow( () -> new AppException(ErrorCode.PAYMENT_TRANSACTION_NOT_FOUND));

    transaction.setStatus(PaymentStatus.SUCCESS);
    transaction.setProviderTransactionId(providerTransactionId);
    transaction.setProviderResponse(providerResponse);
    transaction.setPaidAt(LocalDateTime.now());
    transaction.setUpdatedAt(LocalDateTime.now());

    PaymentTransaction savedTransaction = paymentTransactionRepository.save(transaction);

    UserSubscription subscription = userSubscriptionRepository
        .findById(transaction.getSubscriptionId())
        .orElseThrow( () -> new AppException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND));

    subscription.setStatus(SubscriptionStatus.ACTIVE);
    subscription.setUpdatedAt(LocalDateTime.now());
    userSubscriptionRepository.save(subscription);

    return subscriptionMapper.toResponse(savedTransaction);
  }
}