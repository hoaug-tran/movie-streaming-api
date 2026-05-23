package com.hoaug.movieapi.modules.auth.application.usecase;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.security.BruteForceProtection;
import com.hoaug.movieapi.modules.activitylog.application.service.ActivityLogService;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivityScope;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivitySeverity;
import com.hoaug.movieapi.modules.auth.application.dto.request.LoginRequest;
import com.hoaug.movieapi.modules.auth.application.dto.response.AuthResponse;
import com.hoaug.movieapi.modules.auth.application.dto.response.LoginResult;
import com.hoaug.movieapi.modules.auth.application.dto.response.OtpChallengeResponse;
import com.hoaug.movieapi.modules.auth.application.service.AuthOtpService;
import com.hoaug.movieapi.modules.auth.domain.model.AuthOtpPurpose;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.user.domain.model.AccountStatus;
import com.hoaug.movieapi.modules.user.domain.model.User;

@Component
public class LoginUseCase {
  private final AuthUserRepository authUserRepository;
  private final PasswordEncoder passwordEncoder;
  private final BruteForceProtection bruteForceProtection;
  private final AuthOtpService authOtpService;
  private final TrustedDeviceUseCase trustedDeviceUseCase;
  private final ActivityLogService activityLogService;

  public LoginUseCase(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder,
      BruteForceProtection bruteForceProtection, AuthOtpService authOtpService,
      TrustedDeviceUseCase trustedDeviceUseCase, ActivityLogService activityLogService) {
    this.authUserRepository = authUserRepository;
    this.passwordEncoder = passwordEncoder;
    this.bruteForceProtection = bruteForceProtection;
    this.authOtpService = authOtpService;
    this.trustedDeviceUseCase = trustedDeviceUseCase;
    this.activityLogService = activityLogService;
  }

  public LoginResult execute (LoginRequest request, String trustedDeviceToken) {
    String userIdentifier = request.getUsernameOrEmail();

    if (bruteForceProtection.isLocked(userIdentifier)) {
      throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
    }

    User user = authUserRepository.findByUsername(userIdentifier)
        .or( () -> authUserRepository.findByEmail(userIdentifier)).orElseThrow( () -> {
          bruteForceProtection.recordFailure(userIdentifier);
          return new AppException(ErrorCode.INVALID_CREDENTIALS);
        });

    if (user.getAccountStatus() != AccountStatus.ACTIVE) {
      throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
    }

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      bruteForceProtection.recordFailure(userIdentifier);
      throw new AppException(ErrorCode.INVALID_CREDENTIALS);
    }

    bruteForceProtection.recordSuccess(userIdentifier);

    if (request.isRememberMe()
        && trustedDeviceUseCase.isTrusted(user.getId(), trustedDeviceToken)) {
      AuthResponse directAuth = trustedDeviceUseCase.completeLogin(user);
      activityLogService.record(
        ActivityScope.USER,
        user.getId(),
        user.getFullName(),
        "Đăng nhập",
        "USER",
        user.getId(),
        user.getUsername(),
        user.getFullName() + " đã đăng nhập vào hệ thống bằng thiết bị tin cậy.",
        ActivitySeverity.INFO
      );
      return new LoginResult(directAuth, null);
    }

    LocalDateTime now = LocalDateTime.now();
    user.setLastLoginAt(now);
    user.setUpdatedAt(now);
    authUserRepository.save(user);

    OtpChallengeResponse challenge = authOtpService.issue(AuthOtpPurpose.LOGIN, user.getId(),
        user.getEmail(), user.getFullName(), null);
    return new LoginResult(null, challenge);
  }
}