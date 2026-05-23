package com.hoaug.movieapi.modules.auth.application.usecase;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.activitylog.application.service.ActivityLogService;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivityScope;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivitySeverity;
import com.hoaug.movieapi.modules.auth.application.dto.request.VerifyOtpRequest;
import com.hoaug.movieapi.modules.auth.application.dto.response.AuthResponse;
import com.hoaug.movieapi.modules.auth.application.service.AuthOtpService;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpChallenge;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpPurpose;
import com.hoaug.movieapi.modules.auth.domain.model.RefreshToken;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.auth.domain.service.TokenService;
import com.hoaug.movieapi.modules.email.application.EmailService;
import com.hoaug.movieapi.modules.user.domain.model.AccountStatus;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.mail.MessagingException;

@Component
public class VerifyRegisterOtpUseCase {
  private final AuthOtpService authOtpService;
  private final AuthUserRepository authUserRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenService tokenService;
  private final EmailService emailService;
  private final ActivityLogService activityLogService;

  public VerifyRegisterOtpUseCase(AuthOtpService authOtpService,
      AuthUserRepository authUserRepository, RefreshTokenRepository refreshTokenRepository,
      TokenService tokenService, EmailService emailService, ActivityLogService activityLogService) {
    this.authOtpService = authOtpService;
    this.authUserRepository = authUserRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.tokenService = tokenService;
    this.emailService = emailService;
    this.activityLogService = activityLogService;
  }

  public AuthResponse execute (VerifyOtpRequest request) {
    AuthOtpChallenge challenge = authOtpService.verify(AuthOtpPurpose.REGISTER,
        request.getChallengeToken(), request.getOtp());

    User user = authUserRepository.findById(challenge.getUserId())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    user.setAccountStatus(AccountStatus.ACTIVE);
    user.setUpdatedAt(LocalDateTime.now());
    authUserRepository.save(user);

    try {
      emailService.sendAccountNotificationEmail(user.getEmail(), user.getFullName(),
          "Chào mừng bạn đến với Gió Phim! Tài khoản của bạn đã được kích hoạt thành công.");
    } catch (MessagingException | UnsupportedEncodingException e) {
      org.slf4j.LoggerFactory.getLogger(getClass()).warn("Welcome email failed", e);
    }

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

    activityLogService.record(
      ActivityScope.USER,
      user.getId(),
      user.getFullName(),
      "Đăng ký tài khoản",
      "USER",
      user.getId(),
      user.getUsername(),
      user.getFullName() + " đã đăng ký tài khoản thành công.",
      ActivitySeverity.SUCCESS
    );

    return response;
  }
}
