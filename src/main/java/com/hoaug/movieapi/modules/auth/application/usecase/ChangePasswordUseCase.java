package com.hoaug.movieapi.modules.auth.application.usecase;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.application.dto.request.ChangePasswordRequest;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.user.domain.model.User;

@Component
public class ChangePasswordUseCase {

  private final AuthUserRepository authUserRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;

  public ChangePasswordUseCase(AuthUserRepository authUserRepository,
      RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder) {
    this.authUserRepository = authUserRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void execute (String username, ChangePasswordRequest request) {
    User user = authUserRepository.findByUsername(username)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
      throw new AppException(ErrorCode.INVALID_CREDENTIALS);
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    user.setUpdatedAt(LocalDateTime.now());
    authUserRepository.save(user);

    refreshTokenRepository.findByUserId(user.getId()).forEach(token -> {
      token.setRevokedAt(LocalDateTime.now());
      refreshTokenRepository.save(token);
    });
  }
}
