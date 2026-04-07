package com.hoaug.movieapi.modules.devicesession.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.devicesession.domain.model.DeviceSession;
import com.hoaug.movieapi.modules.devicesession.domain.repository.DeviceSessionRepository;

@Component
public class RevokeAllMyDeviceSessionsUseCase {

  private final DeviceSessionRepository deviceSessionRepository;

  public RevokeAllMyDeviceSessionsUseCase(DeviceSessionRepository deviceSessionRepository) {
    this.deviceSessionRepository = deviceSessionRepository;
  }

  public void execute (Long userId) {
    List<DeviceSession> sessions = deviceSessionRepository
        .findByUserIdAndIsRevokedFalseOrderByLastActiveAtDesc(userId);

    for (DeviceSession session : sessions) {
      session.setIsRevoked(true);
      deviceSessionRepository.save(session);
    }
  }
}