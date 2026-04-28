ALTER TABLE comments
  ADD COLUMN episode_id BIGINT UNSIGNED NULL AFTER movie_id,
  ADD INDEX idx_comments_episode_id (episode_id),
  ADD CONSTRAINT fk_comments_episode FOREIGN KEY (episode_id) REFERENCES episodes (id) ON DELETE SET NULL ON UPDATE CASCADE;
