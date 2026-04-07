package com.hoaug.movieapi.modules.subscription.application.usecase;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.subscription.application.dto.request.CreateInvoiceRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.response.InvoiceResponse;
import com.hoaug.movieapi.modules.subscription.application.mapper.SubscriptionMapper;
import com.hoaug.movieapi.modules.subscription.domain.model.Invoice;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentTransaction;
import com.hoaug.movieapi.modules.subscription.domain.repository.InvoiceRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.PaymentTransactionRepository;

@Component
public class CreateInvoiceUseCase {

  private final InvoiceRepository invoiceRepository;
  private final PaymentTransactionRepository paymentTransactionRepository;
  private final SubscriptionMapper subscriptionMapper;

  public CreateInvoiceUseCase(InvoiceRepository invoiceRepository,
      PaymentTransactionRepository paymentTransactionRepository,
      SubscriptionMapper subscriptionMapper) {
    this.invoiceRepository = invoiceRepository;
    this.paymentTransactionRepository = paymentTransactionRepository;
    this.subscriptionMapper = subscriptionMapper;
  }

  public InvoiceResponse execute (CreateInvoiceRequest request) {
    PaymentTransaction transaction = paymentTransactionRepository
        .findById(request.getPaymentTransactionId())
        .orElseThrow( () -> new AppException(ErrorCode.PAYMENT_TRANSACTION_NOT_FOUND));

    if (transaction.getStatus() != PaymentStatus.SUCCESS) {
      throw new AppException(ErrorCode.PAYMENT_NOT_SUCCESS);
    }

    invoiceRepository.findByPaymentTransactionId(transaction.getId()).ifPresent(invoice -> {
      throw new AppException(ErrorCode.INVOICE_ALREADY_EXISTS);
    });

    Invoice invoice = new Invoice();
    invoice.setPaymentTransactionId(transaction.getId());
    invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    invoice.setBuyerName(request.getBuyerName());
    invoice.setBuyerEmail(request.getBuyerEmail());
    invoice.setAmount(transaction.getAmount());
    invoice.setIssuedAt(LocalDateTime.now());

    return subscriptionMapper.toResponse(invoiceRepository.save(invoice));
  }
}