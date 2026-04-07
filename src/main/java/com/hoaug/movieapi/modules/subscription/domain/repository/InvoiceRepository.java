package com.hoaug.movieapi.modules.subscription.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.subscription.domain.model.Invoice;

public interface InvoiceRepository {

  Optional<Invoice> findById (Long id);

  Optional<Invoice> findByPaymentTransactionId (Long paymentTransactionId);

  Invoice save (Invoice invoice);

  List<Invoice> findAll ();
}