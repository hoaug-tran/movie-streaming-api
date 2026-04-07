package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.subscription.domain.model.Invoice;
import com.hoaug.movieapi.modules.subscription.domain.repository.InvoiceRepository;
import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity.InvoiceEntity;
import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.repository.JpaInvoiceRepository;

@Component
public class InvoiceRepositoryAdapter implements InvoiceRepository {

  private final JpaInvoiceRepository jpaInvoiceRepository;

  public InvoiceRepositoryAdapter(JpaInvoiceRepository jpaInvoiceRepository) {
    this.jpaInvoiceRepository = jpaInvoiceRepository;
  }

  @Override
  public Optional<Invoice> findById (Long id) {
    return jpaInvoiceRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<Invoice> findByPaymentTransactionId (Long paymentTransactionId) {
    return jpaInvoiceRepository.findByPaymentTransactionId(paymentTransactionId)
        .map(this::toDomain);
  }

  @Override
  public Invoice save (Invoice invoice) {
    return toDomain(jpaInvoiceRepository.save(toEntity(invoice)));
  }

  @Override
  public List<Invoice> findAll () {
    return jpaInvoiceRepository.findAll().stream().map(this::toDomain).toList();
  }

  private Invoice toDomain (InvoiceEntity entity) {
    Invoice invoice = new Invoice();
    invoice.setId(entity.getId());
    invoice.setPaymentTransactionId(entity.getPaymentTransactionId());
    invoice.setInvoiceNumber(entity.getInvoiceNumber());
    invoice.setBuyerName(entity.getBuyerName());
    invoice.setBuyerEmail(entity.getBuyerEmail());
    invoice.setAmount(entity.getAmount());
    invoice.setIssuedAt(entity.getIssuedAt());
    return invoice;
  }

  private InvoiceEntity toEntity (Invoice invoice) {
    InvoiceEntity entity = new InvoiceEntity();
    entity.setId(invoice.getId());
    entity.setPaymentTransactionId(invoice.getPaymentTransactionId());
    entity.setInvoiceNumber(invoice.getInvoiceNumber());
    entity.setBuyerName(invoice.getBuyerName());
    entity.setBuyerEmail(invoice.getBuyerEmail());
    entity.setAmount(invoice.getAmount());
    entity.setIssuedAt(invoice.getIssuedAt());
    return entity;
  }
}