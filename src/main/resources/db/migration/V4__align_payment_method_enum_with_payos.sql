ALTER TABLE payment_transactions
  MODIFY payment_method ENUM('PAYOS', 'VNPAY', 'MOMO', 'PAYPAL', 'STRIPE', 'BANK_TRANSFER') NOT NULL;
