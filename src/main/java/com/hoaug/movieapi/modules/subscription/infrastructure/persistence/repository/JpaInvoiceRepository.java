package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity.InvoiceEntity;

public interface JpaInvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

  Optional<InvoiceEntity> findByPaymentTransactionId (Long paymentTransactionId);
}