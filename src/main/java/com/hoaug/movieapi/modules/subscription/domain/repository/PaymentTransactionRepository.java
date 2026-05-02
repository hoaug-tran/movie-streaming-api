package com.hoaug.movieapi.modules.subscription.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.subscription.domain.model.PaymentStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentTransaction;

public interface PaymentTransactionRepository {

  Optional<PaymentTransaction> findById (Long id);

  PaymentTransaction save (PaymentTransaction paymentTransaction);

  List<PaymentTransaction> findByUserIdOrderByCreatedAtDesc (Long userId);

  Optional<PaymentTransaction> findByProviderTransactionId (String providerTransactionId);

  Optional<PaymentTransaction> findBySubscriptionId (Long subscriptionId);

  List<PaymentTransaction> findByStatusAndCreatedAtBefore (PaymentStatus status, LocalDateTime cutoff);
}