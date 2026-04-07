package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoices")
public class InvoiceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "payment_transaction_id")
  private Long paymentTransactionId;

  @Column(name = "invoice_number")
  private String invoiceNumber;

  @Column(name = "buyer_name")
  private String buyerName;

  @Column(name = "buyer_email")
  private String buyerEmail;

  private BigDecimal amount;

  @Column(name = "issued_at")
  private LocalDateTime issuedAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public Long getPaymentTransactionId () {
    return paymentTransactionId;
  }

  public void setPaymentTransactionId (Long paymentTransactionId) {
    this.paymentTransactionId = paymentTransactionId;
  }

  public String getInvoiceNumber () {
    return invoiceNumber;
  }

  public void setInvoiceNumber (String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public String getBuyerName () {
    return buyerName;
  }

  public void setBuyerName (String buyerName) {
    this.buyerName = buyerName;
  }

  public String getBuyerEmail () {
    return buyerEmail;
  }

  public void setBuyerEmail (String buyerEmail) {
    this.buyerEmail = buyerEmail;
  }

  public BigDecimal getAmount () {
    return amount;
  }

  public void setAmount (BigDecimal amount) {
    this.amount = amount;
  }

  public LocalDateTime getIssuedAt () {
    return issuedAt;
  }

  public void setIssuedAt (LocalDateTime issuedAt) {
    this.issuedAt = issuedAt;
  }
}