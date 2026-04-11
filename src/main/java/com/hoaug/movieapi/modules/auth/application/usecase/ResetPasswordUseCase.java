package com.hoaug.movieapi.modules.auth.application.usecase;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.application.dto.request.ResetPasswordRequest;
import com.hoaug.movieapi.modules.auth.domain.model.PasswordResetToken;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.PasswordResetTokenRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.email.application.EmailService;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.mail.MessagingException;

@Component
public class ResetPasswordUseCase {

  private final PasswordResetTokenRepository passwordResetTokenRepository;
  private final AuthUserRepository authUserRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  public ResetPasswordUseCase(PasswordResetTokenRepository passwordResetTokenRepository,
      AuthUserRepository authUserRepository, RefreshTokenRepository refreshTokenRepository,
      PasswordEncoder passwordEncoder, EmailService emailService) {
    this.passwordResetTokenRepository = passwordResetTokenRepository;
    this.authUserRepository = authUserRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  public void execute (ResetPasswordRequest request) {
    PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
        .orElseThrow( () -> new AppException(ErrorCode.INVALID_RESET_TOKEN));

    if (resetToken.getUsedAt() != null || resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new AppException(ErrorCode.INVALID_RESET_TOKEN);
    }

    User user = authUserRepository.findById(resetToken.getUserId())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    user.setUpdatedAt(LocalDateTime.now());
    authUserRepository.save(user);

    try {
      emailService.sendResetPasswordSuccessEmail(user.getEmail(), user.getFullName());
    } catch (MessagingException | UnsupportedEncodingException e) {
      org.slf4j.LoggerFactory.getLogger(this.getClass())
          .warn("Failed to send reset password success email", e);
    }

    resetToken.setUsedAt(LocalDateTime.now());
    passwordResetTokenRepository.save(resetToken);

    refreshTokenRepository.findByUserId(user.getId()).forEach(token -> {
      token.setRevokedAt(LocalDateTime.now());
      refreshTokenRepository.save(token);
    });
  }
}