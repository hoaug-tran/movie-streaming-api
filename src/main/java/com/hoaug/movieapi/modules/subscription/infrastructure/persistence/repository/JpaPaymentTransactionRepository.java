package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity.PaymentTransactionEntity;

public interface JpaPaymentTransactionRepository
    extends JpaRepository<PaymentTransactionEntity, Long> {

  List<PaymentTransactionEntity> findByUserIdOrderByCreatedAtDesc (Long userId);

  Optional<PaymentTransactionEntity> findByProviderTransactionId (String providerTransactionId);
}