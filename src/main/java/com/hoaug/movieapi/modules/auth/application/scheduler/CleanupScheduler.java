package com.hoaug.movieapi.modules.auth.application.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.modules.auth.domain.repository.RefreshTokenRepository;
import com.hoaug.movieapi.modules.devicesession.domain.repository.DeviceSessionRepository;

@Component
public class CleanupScheduler {
  private final RefreshTokenRepository refreshTokenRepository;
  private final DeviceSessionRepository deviceSessionRepository;

  public CleanupScheduler(RefreshTokenRepository refreshTokenRepository,
      DeviceSessionRepository deviceSessionRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.deviceSessionRepository = deviceSessionRepository;
  }

  @Transactional
  @Scheduled(fixedRate = 86400000)
  public void cleanupExpiredTokens () {
    refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
  }

  @Transactional
  @Scheduled(fixedRate = 86400000)
  public void cleanupExpiredSessions () {
    deviceSessionRepository.deleteExpiredSessions(LocalDateTime.now());
  }

  @Scheduled(fixedRate = 604800000)
  public void cleanupDeletedAccounts () {
  }
}
