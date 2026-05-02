package com.hoaug.movieapi.modules.payment.infrastructure.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.payment.application.service.PaymentService;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentTransaction;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;
import com.hoaug.movieapi.modules.subscription.domain.repository.PaymentTransactionRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentReconciliationScheduler {

  private final PaymentTransactionRepository paymentTransactionRepository;
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final PaymentService paymentService;

  @Scheduled(fixedDelayString = "${app.payment.reconciliation.fixed-delay-ms:300000}",
      initialDelayString = "${app.payment.reconciliation.initial-delay-ms:60000}")
  public void reconcilePendingPayments () {
    LocalDateTime cutoff = LocalDateTime.now().minusMinutes(
        PaymentService.PENDING_PAYMENT_TIMEOUT_MINUTES);
    List<PaymentTransaction> pendingTransactions = paymentTransactionRepository
        .findByStatusAndCreatedAtBefore(PaymentStatus.PENDING, cutoff);
    List<UserSubscription> orphanSubscriptions = userSubscriptionRepository
        .findByStatusAndCreatedAtBefore(SubscriptionStatus.PENDING, cutoff).stream()
        .filter(subscription -> paymentTransactionRepository.findBySubscriptionId(subscription.getId())
            .isEmpty())
        .toList();

    if (pendingTransactions.isEmpty() && orphanSubscriptions.isEmpty()) {
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

    cancelOrphanSubscriptions(orphanSubscriptions);
  }

  private void cancelOrphanSubscriptions (List<UserSubscription> orphanSubscriptions) {
    for (UserSubscription subscription : orphanSubscriptions) {
      subscription.setStatus(SubscriptionStatus.CANCELLED);
      userSubscriptionRepository.save(subscription);
      log.warn("Cancelled orphan pending subscription: subscriptionId={}, userId={}",
          subscription.getId(), subscription.getUserId());
    }
  }
}
