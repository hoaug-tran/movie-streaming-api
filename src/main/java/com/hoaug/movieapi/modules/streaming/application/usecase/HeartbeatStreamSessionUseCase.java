package com.hoaug.movieapi.modules.streaming.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.entity.DeviceSessionEntity;
import com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.repository.JpaDeviceSessionRepository;

@Component
public class HeartbeatStreamSessionUseCase {

  private static final int STREAM_TTL_SECONDS = 60;

  private final JpaDeviceSessionRepository deviceSessionRepository;

  public HeartbeatStreamSessionUseCase (JpaDeviceSessionRepository deviceSessionRepository) {
    this.deviceSessionRepository = deviceSessionRepository;
  }

  @Transactional
  public void execute (Long sessionId, Long userId) {
    DeviceSessionEntity session = deviceSessionRepository.findById(sessionId)
        .orElseThrow(() -> new AppException(ErrorCode.FORBIDDEN));

    // Allow guest sessions (userId = null) or authenticated user owning session
    boolean isGuestSession = session.getUserId() == null;
    boolean isUserOwner = userId != null && session.getUserId().equals(userId);
    
    if ((!isGuestSession && !isUserOwner)
        || Boolean.TRUE.equals(session.getIsRevoked())
        || !Boolean.TRUE.equals(session.getIsStreaming())) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    LocalDateTime now = LocalDateTime.now();
    session.setStreamExpiresAt(now.plusSeconds(STREAM_TTL_SECONDS));
    session.setLastActiveAt(now);
    deviceSessionRepository.save(session);
  }
}
