package com.hoaug.movieapi.modules.payment.infrastructure.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.payment.application.service.PaymentService;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentTransaction;
import com.hoaug.movieapi.modules.subscription.domain.repository.PaymentTransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentReconciliationScheduler {

  private static final int PENDING_GRACE_MINUTES = 2;

  private final PaymentTransactionRepository paymentTransactionRepository;
  private final PaymentService paymentService;

  @Scheduled(fixedDelayString = "${app.payment.reconciliation.fixed-delay-ms:300000}",
      initialDelayString = "${app.payment.reconciliation.initial-delay-ms:60000}")
  public void reconcilePendingPayments () {
    LocalDateTime cutoff = LocalDateTime.now().minusMinutes(PENDING_GRACE_MINUTES);
    List<PaymentTransaction> pendingTransactions = paymentTransactionRepository
        .findByStatusAndCreatedAtBefore(PaymentStatus.PENDING, cutoff);

    if (pendingTransactions.isEmpty()) {
      return;
    }

    log.info("Reconciling pending PayOS payments: count={}, cutoff={}", pendingTransactions.size(),
        cutoff);

    for (PaymentTransaction transaction : pendingTransactions) {
      try {
        paymentService.verifyAndSyncPayment(transaction.getProviderTransactionId(),
            "scheduled-reconciliation");
      } catch (Exception e) {
        log.warn("Payment reconciliation skipped for orderCode={}, transactionId={}, error={}",
            transaction.getProviderTransactionId(), transaction.getId(), e.getMessage());
      }
    }
  }
}
