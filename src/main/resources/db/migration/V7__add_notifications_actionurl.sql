-- Migration: Thêm actionUrl và referenceId vào bảng notifications
-- Chạy lệnh này trên MySQL database

ALTER TABLE `notifications`
  ADD COLUMN `action_url` VARCHAR(500) NULL COMMENT 'URL deep-link khi bấm vào thông báo' AFTER `is_read`,
  ADD COLUMN `reference_id` BIGINT UNSIGNED NULL COMMENT 'ID của entity liên quan (movieId, commentId, subscriptionId...)' AFTER `action_url`;

-- Index để query nhanh theo reference_id
CREATE INDEX `idx_notifications_reference_id` ON `notifications` (`reference_id`);
CREATE INDEX `idx_notifications_user_read` ON `notifications` (`user_id`, `is_read`);
