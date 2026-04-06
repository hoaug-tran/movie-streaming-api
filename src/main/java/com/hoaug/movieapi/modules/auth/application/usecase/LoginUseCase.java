package com.hoaug.movieapi.modules.auth.application.usecase;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.application.dto.request.LoginRequest;
import com.hoaug.movieapi.modules.auth.application.dto.response.AuthResponse;
import com.hoaug.movieapi.modules.auth.domain.model.RefreshToken;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.auth.domain.service.TokenService;
import com.hoaug.movieapi.modules.user.domain.model.AccountStatus;
import com.hoaug.movieapi.modules.user.domain.model.User;

@Component
public class LoginUseCase {
  private final AuthUserRepository authUserRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;

  public LoginUseCase(AuthUserRepository authUserRepository,
      RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder,
      TokenService tokenService) {
    this.authUserRepository = authUserRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenService = tokenService;
  }

  public AuthResponse execute (LoginRequest request) {
    User user = authUserRepository.findByUsername(request.getUsernameOrEmail())
        .or( () -> authUserRepository.findByEmail(request.getUsernameOrEmail()))
        .orElseThrow( () -> new AppException(ErrorCode.INVALID_CREDENTIALS));

    if (user.getAccountStatus() != AccountStatus.ACTIVE) {
      throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
    }

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new AppException(ErrorCode.INVALID_CREDENTIALS);
    }

    LocalDateTime now = LocalDateTime.now();

    user.setLastLoginAt(now);
    user.setUpdatedAt(now);
    authUserRepository.save(user);

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
}