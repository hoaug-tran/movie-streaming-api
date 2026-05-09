package com.hoaug.movieapi.modules.auth.application.usecase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.auth.application.dto.response.AuthResponse;
import com.hoaug.movieapi.modules.auth.domain.model.RefreshToken;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.auth.domain.service.TokenService;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class TrustedDeviceUseCase {
  private static final String COOKIE_NAME = "trusted_device";
  private static final int COOKIE_MAX_AGE = (int) Duration.ofDays(90).toSeconds();

  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenService tokenService;

  public TrustedDeviceUseCase(RefreshTokenRepository refreshTokenRepository,
      TokenService tokenService) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.tokenService = tokenService;
  }

  public boolean isTrusted (Long userId, HttpServletRequest httpRequest) {
    String token = extractDeviceCookie(httpRequest);
    if (token == null) {
      return false;
    }
    return refreshTokenRepository.findByToken(token)
        .map(rt -> rt.getUserId().equals(userId) && rt.getRevokedAt() == null
            && rt.getExpiresAt().isAfter(LocalDateTime.now()))
        .orElse(false);
  }

  public void trust (Long userId, HttpServletRequest httpRequest) {
    String deviceToken = UUID.randomUUID().toString() + UUID.randomUUID().toString();
    LocalDateTime now = LocalDateTime.now();

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUserId(userId);
    refreshToken.setToken(deviceToken);
    refreshToken.setExpiresAt(now.plusDays(90));
    refreshToken.setCreatedAt(now);
    refreshTokenRepository.save(refreshToken);

    jakarta.servlet.http.HttpServletResponse response = null;
    if (httpRequest.getAttribute("javax.servlet.http.HttpServletResponse") instanceof jakarta.servlet.http.HttpServletResponse r) {
      response = r;
    }
  }

  public AuthResponse completeLogin (User user, boolean rememberMe,
      HttpServletRequest httpRequest) {
    LocalDateTime now = LocalDateTime.now();

    String accessToken = tokenService.generateAccessToken(user.getUsername());
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

  public Cookie buildDeviceCookie (String deviceToken) {
    Cookie cookie = new Cookie(COOKIE_NAME, deviceToken);
    cookie.setHttpOnly(true);
    cookie.setSecure(false);
    cookie.setPath("/");
    cookie.setMaxAge(COOKIE_MAX_AGE);
    return cookie;
  }

  private String extractDeviceCookie (HttpServletRequest httpRequest) {
    if (httpRequest.getCookies() == null) {
      return null;
    }
    for (Cookie cookie : httpRequest.getCookies()) {
      if (COOKIE_NAME.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }
}
