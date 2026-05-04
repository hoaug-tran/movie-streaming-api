-- Align MySQL enum values with Java PaymentMethod enum.
-- Root cause: payment_transactions.payment_method was created without PAYOS,
-- so inserting PaymentMethod.PAYOS caused MySQL data truncation.
ALTER TABLE payment_transactions
  MODIFY payment_method ENUM('PAYOS', 'VNPAY', 'MOMO', 'PAYPAL', 'STRIPE', 'BANK_TRANSFER') NOT NULL;
