ALTER TABLE episodes
  ADD COLUMN available_qualities VARCHAR(100) DEFAULT NULL;

ALTER TABLE device_sessions
  ADD COLUMN is_streaming     TINYINT(1) NOT NULL DEFAULT 0,
  ADD COLUMN stream_expires_at DATETIME  DEFAULT NULL,
  ADD KEY idx_device_sessions_streaming (user_id, is_streaming, stream_expires_at);
