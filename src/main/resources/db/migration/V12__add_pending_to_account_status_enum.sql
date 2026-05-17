-- Add PENDING value to account_status enum to match AccountStatus.java
-- This fixes registration failure: "Data truncated for column 'account_status'"
ALTER TABLE users
    MODIFY COLUMN account_status ENUM('ACTIVE', 'PENDING', 'BLOCKED', 'DELETED') NOT NULL;
