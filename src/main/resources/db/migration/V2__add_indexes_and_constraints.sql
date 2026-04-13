CREATE INDEX idx_user_username ON users(username);
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_user_role ON users(role);
CREATE INDEX idx_user_account_status ON users(account_status);
CREATE INDEX idx_user_created_at ON users(created_at);

CREATE INDEX idx_movie_title ON movies(title);
CREATE INDEX idx_movie_status ON movies(status);
CREATE INDEX idx_movie_release_date ON movies(release_date);
CREATE INDEX idx_movie_created_at ON movies(created_at);

CREATE INDEX idx_episode_movie_id ON episodes(movie_id);
CREATE INDEX idx_episode_episode_number ON episodes(movie_id, episode_number);

CREATE INDEX idx_comment_movie_id ON comments(movie_id);
CREATE INDEX idx_comment_user_id ON comments(user_id);
CREATE INDEX idx_comment_parent_id ON comments(parent_comment_id);
CREATE INDEX idx_comment_created_at ON comments(created_at);

CREATE INDEX idx_review_movie_id ON reviews(movie_id);
CREATE INDEX idx_review_user_id ON reviews(user_id);
CREATE INDEX idx_review_created_at ON reviews(created_at);

CREATE INDEX idx_watch_history_user_id ON watch_histories(user_id);
CREATE INDEX idx_watch_history_movie_id ON watch_histories(movie_id);
CREATE INDEX idx_watch_history_episode_id ON watch_histories(episode_id);
CREATE INDEX idx_watch_history_last_watched ON watch_histories(last_watched_at);

CREATE INDEX idx_watchlist_user_id ON watchlists(user_id);
CREATE INDEX idx_watchlist_movie_id ON watchlists(movie_id);

CREATE INDEX idx_favorite_user_id ON favorites(user_id);
CREATE INDEX idx_favorite_movie_id ON favorites(movie_id);

CREATE INDEX idx_subscription_user_id ON subscriptions(user_id);
CREATE INDEX idx_subscription_plan_id ON subscriptions(plan_id);
CREATE INDEX idx_subscription_status ON subscriptions(status);
CREATE INDEX idx_subscription_expiry ON subscriptions(expiry_date);

CREATE INDEX idx_search_history_user_id ON search_histories(user_id);
CREATE INDEX idx_search_history_created_at ON search_histories(created_at);

CREATE INDEX idx_notification_user_id ON notifications(user_id);
CREATE INDEX idx_notification_is_read ON notifications(is_read);

CREATE INDEX idx_device_session_user_id ON device_sessions(user_id);
CREATE INDEX idx_device_session_status ON device_sessions(status);

CREATE INDEX idx_refresh_token_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_token_token ON refresh_tokens(token, user_id);

CREATE INDEX idx_report_user_id ON reports(user_id);
CREATE INDEX idx_report_status ON reports(status);
CREATE INDEX idx_report_created_at ON reports(created_at);

ALTER TABLE users ADD CONSTRAINT uc_user_username UNIQUE(username);
ALTER TABLE users ADD CONSTRAINT uc_user_email UNIQUE(email);

ALTER TABLE movies ADD CONSTRAINT uc_movie_title_year UNIQUE(title, release_year);

ALTER TABLE categories ADD CONSTRAINT uc_category_name UNIQUE(name);

ALTER TABLE tags ADD CONSTRAINT uc_tag_name UNIQUE(name);

ALTER TABLE studios ADD CONSTRAINT uc_studio_name UNIQUE(name);

ALTER TABLE persons ADD CONSTRAINT uc_person_name UNIQUE(name);

ALTER TABLE watch_histories ADD CONSTRAINT uc_watch_history_user_episode UNIQUE(user_id, episode_id);

ALTER TABLE subscriptions ADD CONSTRAINT uc_subscription_active UNIQUE(user_id) 
  WHERE status = 'ACTIVE';

ALTER TABLE device_sessions ADD CONSTRAINT uc_device_session_user_device UNIQUE(user_id, device_id);
