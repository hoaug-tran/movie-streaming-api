package com.hoaug.movieapi.modules.auth.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.domain.model.RefreshToken;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;

@Component
public class LogoutUseCase {

  private final RefreshTokenRepository refreshTokenRepository;

  public LogoutUseCase(RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
  }

  public void execute (String refreshTokenValue) {
    RefreshToken token = refreshTokenRepository.findByToken(refreshTokenValue)
        .orElseThrow( () -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

    token.setRevokedAt(LocalDateTime.now());
    refreshTokenRepository.save(token);
  }
}
