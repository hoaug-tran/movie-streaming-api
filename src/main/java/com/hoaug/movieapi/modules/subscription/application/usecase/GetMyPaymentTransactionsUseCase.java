package com.hoaug.movieapi.modules.subscription.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.subscription.application.dto.response.PaymentTransactionResponse;
import com.hoaug.movieapi.modules.subscription.application.mapper.SubscriptionMapper;
import com.hoaug.movieapi.modules.subscription.domain.repository.PaymentTransactionRepository;

@Component
public class GetMyPaymentTransactionsUseCase {

  private final PaymentTransactionRepository paymentTransactionRepository;
  private final SubscriptionMapper subscriptionMapper;

  public GetMyPaymentTransactionsUseCase(PaymentTransactionRepository paymentTransactionRepository,
      SubscriptionMapper subscriptionMapper) {
    this.paymentTransactionRepository = paymentTransactionRepository;
    this.subscriptionMapper = subscriptionMapper;
  }

  public List<PaymentTransactionResponse> execute (Long userId) {
    return paymentTransactionRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
        .map(subscriptionMapper::toResponse).toList();
  }
}