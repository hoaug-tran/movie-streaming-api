ALTER TABLE `users`
ADD COLUMN `oauth_id` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `password`,
ADD COLUMN `oauth_provider` VARCHAR(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `oauth_id`,
ADD COLUMN `profile_picture_url` VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `avatar_url`,
ADD UNIQUE KEY `uk_users_oauth_id_provider` (`oauth_id`, `oauth_provider`);

CREATE TABLE `oauth_providers` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL UNIQUE,
  `display_name` VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `client_id` VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `client_secret` VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `authorization_uri` VARCHAR(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token_uri` VARCHAR(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_info_uri` VARCHAR(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `redirect_uri` VARCHAR(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `scopes` VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` TINYINT(1) NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_oauth_providers_name` (`name`),
  KEY `idx_oauth_providers_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `oauth_credentials` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `provider_id` bigint unsigned NOT NULL,
  `oauth_id` VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `access_token` LONGTEXT COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `refresh_token` LONGTEXT COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `token_expiry` DATETIME DEFAULT NULL,
  `id_token` LONGTEXT COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `profile_data` JSON DEFAULT NULL,
  `connected_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_used_at` DATETIME DEFAULT NULL,
  `is_active` TINYINT(1) NOT NULL DEFAULT 1,
  `created_at` DATETIME(6) NOT NULL,
  `updated_at` DATETIME(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_oauth_credentials_user_provider_oauth` (`user_id`, `provider_id`, `oauth_id`),
  KEY `idx_oauth_credentials_oauth_id` (`oauth_id`),
  KEY `idx_oauth_credentials_provider_id` (`provider_id`),
  KEY `idx_oauth_credentials_is_active` (`is_active`),
  CONSTRAINT `fk_oauth_credentials_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_oauth_credentials_provider` FOREIGN KEY (`provider_id`) REFERENCES `oauth_providers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `oauth_providers` 
(`name`, `display_name`, `client_id`, `client_secret`, `authorization_uri`, `token_uri`, `user_info_uri`, `redirect_uri`, `scopes`, `is_active`)
VALUES
('google', 'Google', 'GOOGLE_CLIENT_ID_PLACEHOLDER', 'GOOGLE_CLIENT_SECRET_PLACEHOLDER', 
 'https://accounts.google.com/o/oauth2/v2/auth',
 'https://www.googleapis.com/oauth2/v4/token',
 'https://www.googleapis.com/oauth2/v1/userinfo',
 'http://localhost:3000/auth/callback/google',
 'openid email profile',
 1);
