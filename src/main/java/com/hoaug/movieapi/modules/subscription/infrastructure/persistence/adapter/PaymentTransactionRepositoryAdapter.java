package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.subscription.domain.model.PaymentTransaction;
import com.hoaug.movieapi.modules.subscription.domain.repository.PaymentTransactionRepository;
import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity.PaymentTransactionEntity;
import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.repository.JpaPaymentTransactionRepository;

@Component
public class PaymentTransactionRepositoryAdapter implements PaymentTransactionRepository {

  private final JpaPaymentTransactionRepository jpaPaymentTransactionRepository;

  public PaymentTransactionRepositoryAdapter(
      JpaPaymentTransactionRepository jpaPaymentTransactionRepository) {
    this.jpaPaymentTransactionRepository = jpaPaymentTransactionRepository;
  }

  @Override
  public Optional<PaymentTransaction> findById (Long id) {
    return jpaPaymentTransactionRepository.findById(id).map(this::toDomain);
  }

  @Override
  public PaymentTransaction save (PaymentTransaction paymentTransaction) {
    return toDomain(jpaPaymentTransactionRepository.save(toEntity(paymentTransaction)));
  }

  @Override
  public List<PaymentTransaction> findByUserIdOrderByCreatedAtDesc (Long userId) {
    return jpaPaymentTransactionRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
        .map(this::toDomain).toList();
  }

  @Override
  public Optional<PaymentTransaction> findByProviderTransactionId (String providerTransactionId) {
    return jpaPaymentTransactionRepository.findByProviderTransactionId(providerTransactionId)
        .map(this::toDomain);
  }

  private PaymentTransaction toDomain (PaymentTransactionEntity entity) {
    PaymentTransaction transaction = new PaymentTransaction();
    transaction.setId(entity.getId());
    transaction.setUserId(entity.getUserId());
    transaction.setSubscriptionId(entity.getSubscriptionId());
    transaction.setAmount(entity.getAmount());
    transaction.setCurrency(entity.getCurrency());
    transaction.setPaymentMethod(entity.getPaymentMethod());
    transaction.setStatus(entity.getStatus());
    transaction.setProviderTransactionId(entity.getProviderTransactionId());
    transaction.setProviderResponse(entity.getProviderResponse());
    transaction.setPaidAt(entity.getPaidAt());
    transaction.setCreatedAt(entity.getCreatedAt());
    transaction.setUpdatedAt(entity.getUpdatedAt());
    return transaction;
  }

  private PaymentTransactionEntity toEntity (PaymentTransaction transaction) {
    PaymentTransactionEntity entity = new PaymentTransactionEntity();
    entity.setId(transaction.getId());
    entity.setUserId(transaction.getUserId());
    entity.setSubscriptionId(transaction.getSubscriptionId());
    entity.setAmount(transaction.getAmount());
    entity.setCurrency(transaction.getCurrency());
    entity.setPaymentMethod(transaction.getPaymentMethod());
    entity.setStatus(transaction.getStatus());
    entity.setProviderTransactionId(transaction.getProviderTransactionId());
    entity.setProviderResponse(transaction.getProviderResponse());
    entity.setPaidAt(transaction.getPaidAt());
    entity.setCreatedAt(transaction.getCreatedAt());
    entity.setUpdatedAt(transaction.getUpdatedAt());
    return entity;
  }
}