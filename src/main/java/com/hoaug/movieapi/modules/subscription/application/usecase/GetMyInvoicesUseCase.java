package com.hoaug.movieapi.modules.subscription.application.usecase;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.subscription.application.dto.response.InvoiceResponse;
import com.hoaug.movieapi.modules.subscription.application.mapper.SubscriptionMapper;
import com.hoaug.movieapi.modules.subscription.domain.repository.InvoiceRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.PaymentTransactionRepository;

@Component
public class GetMyInvoicesUseCase {

  private final InvoiceRepository invoiceRepository;
  private final PaymentTransactionRepository paymentTransactionRepository;
  private final SubscriptionMapper subscriptionMapper;

  public GetMyInvoicesUseCase(InvoiceRepository invoiceRepository,
      PaymentTransactionRepository paymentTransactionRepository,
      SubscriptionMapper subscriptionMapper) {
    this.invoiceRepository = invoiceRepository;
    this.paymentTransactionRepository = paymentTransactionRepository;
    this.subscriptionMapper = subscriptionMapper;
  }

  public List<InvoiceResponse> execute (Long userId) {
    Set<Long> myTransactionIds = paymentTransactionRepository
        .findByUserIdOrderByCreatedAtDesc(userId).stream().map(item -> item.getId())
        .collect(Collectors.toSet());

    return invoiceRepository.findAll().stream()
        .filter(invoice -> myTransactionIds.contains(invoice.getPaymentTransactionId()))
        .map(subscriptionMapper::toResponse).toList();
  }
}