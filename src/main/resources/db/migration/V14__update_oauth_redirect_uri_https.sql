UPDATE oauth_providers
SET redirect_uri = 'https://giophim.libsys.me/auth/callback/google',
    updated_at = NOW()
WHERE name = 'google';
