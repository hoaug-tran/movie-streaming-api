package com.hoaug.movieapi.modules.auth.application.usecase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.util.CookieUtil;
import com.hoaug.movieapi.modules.auth.application.dto.response.AuthResponse;
import com.hoaug.movieapi.modules.auth.domain.model.RefreshToken;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.auth.domain.service.TokenService;
import com.hoaug.movieapi.modules.user.domain.model.User;


@Component
public class TrustedDeviceUseCase {
  private static final String COOKIE_NAME = "trusted_device";
  private static final Duration COOKIE_MAX_AGE = Duration.ofDays(90);

  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenService tokenService;
  private final CookieUtil cookieUtil;

  public TrustedDeviceUseCase(RefreshTokenRepository refreshTokenRepository,
      TokenService tokenService, CookieUtil cookieUtil) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.tokenService = tokenService;
    this.cookieUtil = cookieUtil;
  }

  public boolean isTrusted (Long userId, String token) {
    if (token == null || token.isBlank()) {
      return false;
    }
    return refreshTokenRepository.findByToken(token)
        .map(rt -> rt.getUserId().equals(userId) && rt.getRevokedAt() == null
            && rt.getExpiresAt().isAfter(LocalDateTime.now()))
        .orElse(false);
  }

  public ResponseCookie trust (Long userId) {
    String deviceToken = UUID.randomUUID().toString() + UUID.randomUUID().toString();
    LocalDateTime now = LocalDateTime.now();

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUserId(userId);
    refreshToken.setToken(deviceToken);
    refreshToken.setExpiresAt(now.plusDays(90));
    refreshToken.setCreatedAt(now);
    refreshTokenRepository.save(refreshToken);

    return buildDeviceCookie(deviceToken);
  }

  public AuthResponse completeLogin (User user) {
    LocalDateTime now = LocalDateTime.now();

    String accessToken = tokenService.generateAccessToken(user.getUsername(), user.getRole().name());
    String refreshTokenValue = tokenService.generateRefreshToken();

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUserId(user.getId());
    refreshToken.setToken(refreshTokenValue);
    refreshToken.setExpiresAt(now.plusDays(30));
    refreshToken.setCreatedAt(now);
    refreshTokenRepository.save(refreshToken);

    AuthResponse response = new AuthResponse();
    response.setAccessToken(accessToken);
    response.setRefreshToken(refreshTokenValue);
    response.setTokenType("Bearer");
    response.setUserId(user.getId());
    response.setUsername(user.getUsername());
    response.setEmail(user.getEmail());
    response.setFullName(user.getFullName());
    response.setRole(user.getRole());
    return response;
  }

  public ResponseCookie buildDeviceCookie (String deviceToken) {
    return cookieUtil.buildCookie(COOKIE_NAME, deviceToken, COOKIE_MAX_AGE);
  }
}
