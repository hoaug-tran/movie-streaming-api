package com.hoaug.movieapi.modules.streaming.application.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.repository.JpaDeviceSessionRepository;

@Component
public class StopStreamSessionUseCase {

  private final JpaDeviceSessionRepository deviceSessionRepository;

  public StopStreamSessionUseCase (JpaDeviceSessionRepository deviceSessionRepository) {
    this.deviceSessionRepository = deviceSessionRepository;
  }

  @Transactional
  public void execute (Long sessionId, Long userId) {
    deviceSessionRepository.findById(sessionId).ifPresent(session -> {
      if (session.getUserId().equals(userId)) {
        session.setIsStreaming(false);
        deviceSessionRepository.save(session);
      }
    });
  }
}
