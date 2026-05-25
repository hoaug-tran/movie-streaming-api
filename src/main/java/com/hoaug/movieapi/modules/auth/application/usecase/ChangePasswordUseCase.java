package com.hoaug.movieapi.modules.auth.application.usecase;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.application.dto.request.ChangePasswordRequest;
import com.hoaug.movieapi.modules.auth.application.service.AuthOtpService;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpChallenge;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpPurpose;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.email.application.EmailService;
import com.hoaug.movieapi.modules.user.domain.model.User;



@Component
public class ChangePasswordUseCase {

  private final AuthUserRepository authUserRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final AuthOtpService authOtpService;

  public ChangePasswordUseCase(AuthUserRepository authUserRepository,
      RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder,
      EmailService emailService, AuthOtpService authOtpService) {
    this.authUserRepository = authUserRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.authOtpService = authOtpService;
  }

  public void execute (String username, ChangePasswordRequest request) {
    User user = authUserRepository.findByUsername(username)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new AppException(ErrorCode.INVALID_CREDENTIALS);
    }

    AuthOtpChallenge challenge = authOtpService.verify(AuthOtpPurpose.PASSWORD_CHANGE,
        request.getChallengeToken(), request.getOtp());
    if (!user.getId().equals(challenge.getUserId())) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    user.setUpdatedAt(LocalDateTime.now());
    authUserRepository.save(user);

    try {
      emailService.sendAccountNotificationEmail(user.getEmail(), user.getFullName(),
          "Mật khẩu của bạn đã được thay đổi thành công. Nếu đây không phải yêu cầu của bạn, vui lòng liên hệ với chúng tôi ngay.");
    } catch (IOException e) {
      org.slf4j.LoggerFactory.getLogger(this.getClass())
          .warn("Failed to send password change notification email", e);
    }

    refreshTokenRepository.findByUserId(user.getId()).forEach(token -> {
      token.setRevokedAt(LocalDateTime.now());
      refreshTokenRepository.save(token);
    });
  }
}
