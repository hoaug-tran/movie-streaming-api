-- Insert default OAuth providers
INSERT INTO oauth_providers (name, display_name, client_id, client_secret, authorization_uri, token_uri, user_info_uri, redirect_uri, scopes, is_active, created_at, updated_at)
VALUES (
  'google',
  'Google',
  'PLACEHOLDER_GOOGLE_CLIENT_ID',
  'PLACEHOLDER_GOOGLE_CLIENT_SECRET',
  'https://accounts.google.com/o/oauth2/v2/auth',
  'https://oauth2.googleapis.com/token',
  'https://www.googleapis.com/oauth2/v1/userinfo',
  'http://localhost:3000/auth/callback/google',
  'openid email profile',
  1,
  NOW(),
  NOW()
) ON DUPLICATE KEY UPDATE updated_at = NOW();
