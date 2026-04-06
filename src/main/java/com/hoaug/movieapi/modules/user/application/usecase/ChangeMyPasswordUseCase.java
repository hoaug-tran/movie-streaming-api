package com.hoaug.movieapi.modules.user.application.usecase;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.user.application.dto.request.ChangePasswordRequest;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class ChangeMyPasswordUseCase {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  private final RefreshTokenRepository refreshTokenRepository;

  public ChangeMyPasswordUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder,
      RefreshTokenRepository refreshTokenRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  public void execute (String username, ChangePasswordRequest request) {
    User user = userRepository.findByUsername(username)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
      throw new AppException(ErrorCode.WRONG_PASSWORD);
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    user.setUpdatedAt(LocalDateTime.now());
    userRepository.save(user);

    refreshTokenRepository.findByUserId(user.getId()).forEach(token -> {
      token.setRevokedAt(LocalDateTime.now());
      refreshTokenRepository.save(token);
    });
  }
}
