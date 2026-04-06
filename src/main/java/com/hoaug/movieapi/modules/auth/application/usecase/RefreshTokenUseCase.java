package com.hoaug.movieapi.modules.auth.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.application.dto.request.RefreshTokenRequest;
import com.hoaug.movieapi.modules.auth.application.dto.response.RefreshTokenResponse;
import com.hoaug.movieapi.modules.auth.domain.model.RefreshToken;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.auth.domain.service.TokenService;
import com.hoaug.movieapi.modules.user.domain.model.User;

@Component
public class RefreshTokenUseCase {

  private final RefreshTokenRepository refreshTokenRepository;
  private final AuthUserRepository authUserRepository;
  private final TokenService tokenService;

  public RefreshTokenUseCase(RefreshTokenRepository refreshTokenRepository,
      AuthUserRepository authUserRepository, TokenService tokenService) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.authUserRepository = authUserRepository;
    this.tokenService = tokenService;
  }

  public RefreshTokenResponse execute (RefreshTokenRequest request) {
    RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
        .orElseThrow( () -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

    if (refreshToken.getRevokedAt() != null
        || refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    User user = authUserRepository.findById(refreshToken.getUserId())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    refreshToken.setRevokedAt(LocalDateTime.now());
    refreshTokenRepository.save(refreshToken);

    String newRefreshTokenValue = tokenService.generateRefreshToken();

    RefreshToken newRefreshToken = new RefreshToken();
    newRefreshToken.setUserId(user.getId());
    newRefreshToken.setToken(newRefreshTokenValue);
    newRefreshToken.setExpiresAt(LocalDateTime.now().plusDays(30));
    newRefreshToken.setCreatedAt(LocalDateTime.now());
    refreshTokenRepository.save(newRefreshToken);

    RefreshTokenResponse response = new RefreshTokenResponse();
    response.setAccessToken(tokenService.generateAccessToken(user.getUsername()));
    response.setRefreshToken(newRefreshTokenValue);
    response.setTokenType("Bearer");

    return response;
  }
}
