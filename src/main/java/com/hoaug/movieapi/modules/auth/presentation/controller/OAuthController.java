package com.hoaug.movieapi.modules.auth.presentation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import com.hoaug.movieapi.modules.auth.application.dto.oauth.GoogleOAuthUserInfo;
import com.hoaug.movieapi.modules.auth.application.dto.response.AuthResponse;
import com.hoaug.movieapi.modules.auth.application.port.GoogleOAuthClient;
import com.hoaug.movieapi.modules.auth.application.usecase.ExchangeOAuthTokenUseCase;

@RestController
@RequestMapping("/api/v1/auth/oauth")
public class OAuthController {
  private final ExchangeOAuthTokenUseCase exchangeOAuthTokenUseCase;
  private final GoogleOAuthClient googleOAuthClient;
  private final CookieUtil cookieUtil;

  public OAuthController(ExchangeOAuthTokenUseCase exchangeOAuthTokenUseCase,
      GoogleOAuthClient googleOAuthClient, CookieUtil cookieUtil) {
    this.exchangeOAuthTokenUseCase = exchangeOAuthTokenUseCase;
    this.googleOAuthClient = googleOAuthClient;
    this.cookieUtil = cookieUtil;
  }

  @PostMapping("/callback/google")
  public ResponseEntity<AuthResponse> googleCallback (@RequestBody Map<String, String> request,
      HttpServletResponse response) {
    String code = request.get("code");

    if (code == null || code.isBlank()) {
      throw new AppException(ErrorCode.VALIDATION_ERROR);
    }

    GoogleOAuthUserInfo googleUserInfo = googleOAuthClient.exchangeCode(code.trim());

    AuthResponse authResponse = exchangeOAuthTokenUseCase.execute("google", googleUserInfo.getSub(),
        googleUserInfo.getProfileData(), googleUserInfo.getAccessToken(),
        googleUserInfo.getRefreshToken(), googleUserInfo.getTokenExpiry(),
        googleUserInfo.getIdToken());

    cookieUtil.setAuthCookies(response, authResponse);

    return ResponseUtil.ok(authResponse);
  }

  @GetMapping("/providers")
  public ResponseEntity<Map<String, String>> getProviders () {
    Map<String, String> providers = new HashMap<>();
    providers.put("google", "Google");
    return ResponseUtil.ok(providers);
  }
}