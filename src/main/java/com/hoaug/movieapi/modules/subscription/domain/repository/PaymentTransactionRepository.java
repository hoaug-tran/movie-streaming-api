package com.hoaug.movieapi.modules.subscription.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.subscription.domain.model.PaymentTransaction;

public interface PaymentTransactionRepository {

  Optional<PaymentTransaction> findById (Long id);

  PaymentTransaction save (PaymentTransaction paymentTransaction);

  List<PaymentTransaction> findByUserIdOrderByCreatedAtDesc (Long userId);

  Optional<PaymentTransaction> findByProviderTransactionId (String providerTransactionId);
}