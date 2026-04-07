package com.hoaug.movieapi.modules.devicesession.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.devicesession.domain.model.DeviceSession;
import com.hoaug.movieapi.modules.devicesession.domain.repository.DeviceSessionRepository;

@Component
public class RevokeDeviceSessionUseCase {

  private final DeviceSessionRepository deviceSessionRepository;

  public RevokeDeviceSessionUseCase(DeviceSessionRepository deviceSessionRepository) {
    this.deviceSessionRepository = deviceSessionRepository;
  }

  public void execute (Long userId, Long sessionId) {
    DeviceSession deviceSession = deviceSessionRepository.findById(sessionId)
        .orElseThrow( () -> new AppException(ErrorCode.DEVICE_SESSION_NOT_FOUND));

    if (!deviceSession.getUserId().equals(userId)) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    deviceSession.setIsRevoked(true);
    deviceSessionRepository.save(deviceSession);
  }
}