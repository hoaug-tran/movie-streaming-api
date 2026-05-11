package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hoaug.movieapi.modules.subscription.domain.model.PaymentStatus;
import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity.PaymentTransactionEntity;

public interface JpaPaymentTransactionRepository
    extends JpaRepository<PaymentTransactionEntity, Long> {

  List<PaymentTransactionEntity> findByUserIdOrderByCreatedAtDesc (Long userId);

  Optional<PaymentTransactionEntity> findByProviderTransactionId (String providerTransactionId);

  Optional<PaymentTransactionEntity> findBySubscriptionId (Long subscriptionId);

  List<PaymentTransactionEntity> findByStatusAndCreatedAtBefore (PaymentStatus status,
      LocalDateTime cutoff);

  long countByStatus (PaymentStatus status);

  long countByStatusAndPaidAtAfter (PaymentStatus status, LocalDateTime paidAt);

  @Query("select coalesce(sum(p.amount), 0) from PaymentTransactionEntity p where p.status = :status")
  BigDecimal sumAmountByStatus (@Param("status") PaymentStatus status);

  @Query("select coalesce(sum(p.amount), 0) from PaymentTransactionEntity p where p.status = :status and p.paidAt >= :paidAt")
  BigDecimal sumAmountByStatusAndPaidAtAfter (@Param("status") PaymentStatus status,
      @Param("paidAt") LocalDateTime paidAt);
}