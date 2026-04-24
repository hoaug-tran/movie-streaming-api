package com.hoaug.movieapi.modules.auth.application.port;

import com.hoaug.movieapi.modules.auth.application.dto.oauth.GoogleOAuthUserInfo;

public interface GoogleOAuthClient {
  GoogleOAuthUserInfo exchangeCode (String code);
}