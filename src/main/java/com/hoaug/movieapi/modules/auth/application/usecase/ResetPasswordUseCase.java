package com.hoaug.movieapi.modules.auth.application.usecase;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.application.dto.request.VerifyPasswordResetOtpRequest;
import com.hoaug.movieapi.modules.auth.application.service.AuthOtpService;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpChallenge;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpPurpose;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.email.application.EmailService;
import com.hoaug.movieapi.modules.user.domain.model.User;



@Component
public class ResetPasswordUseCase {
  private final AuthOtpService authOtpService;
  private final AuthUserRepository authUserRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  public ResetPasswordUseCase(AuthOtpService authOtpService, AuthUserRepository authUserRepository,
      RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder,
      EmailService emailService) {
    this.authOtpService = authOtpService;
    this.authUserRepository = authUserRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  public void execute (VerifyPasswordResetOtpRequest request) {
    AuthOtpChallenge challenge = authOtpService.verify(AuthOtpPurpose.PASSWORD_RESET,
        request.getChallengeToken(), request.getOtp());

    User user = authUserRepository.findById(challenge.getUserId())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    user.setUpdatedAt(LocalDateTime.now());
    authUserRepository.save(user);

    refreshTokenRepository.findByUserId(user.getId()).forEach(token -> {
      token.setRevokedAt(LocalDateTime.now());
      refreshTokenRepository.save(token);
    });

    try {
      emailService.sendResetPasswordSuccessEmail(user.getEmail(), user.getFullName());
    } catch (IOException e) {
      org.slf4j.LoggerFactory.getLogger(getClass()).warn("Reset success email failed", e);
    }
  }
}