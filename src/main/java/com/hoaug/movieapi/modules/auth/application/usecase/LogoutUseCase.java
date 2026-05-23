package com.hoaug.movieapi.modules.auth.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.activitylog.application.service.ActivityLogService;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivityScope;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivitySeverity;
import com.hoaug.movieapi.modules.auth.domain.model.RefreshToken;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;

@Component
public class LogoutUseCase {

  private final RefreshTokenRepository refreshTokenRepository;
  private final AuthUserRepository authUserRepository;
  private final ActivityLogService activityLogService;

  public LogoutUseCase(RefreshTokenRepository refreshTokenRepository,
      AuthUserRepository authUserRepository, ActivityLogService activityLogService) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.authUserRepository = authUserRepository;
    this.activityLogService = activityLogService;
  }

  public void execute (String refreshTokenValue) {
    RefreshToken token = refreshTokenRepository.findByToken(refreshTokenValue)
        .orElseThrow( () -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

    token.setRevokedAt(LocalDateTime.now());
    refreshTokenRepository.save(token);

    authUserRepository.findById(token.getUserId()).ifPresent(user -> {
      activityLogService.record(
        ActivityScope.USER,
        user.getId(),
        user.getFullName(),
        "Đăng xuất",
        "USER",
        user.getId(),
        user.getUsername(),
        user.getFullName() + " đã đăng xuất khỏi hệ thống.",
        ActivitySeverity.INFO
      );
    });
  }
}
