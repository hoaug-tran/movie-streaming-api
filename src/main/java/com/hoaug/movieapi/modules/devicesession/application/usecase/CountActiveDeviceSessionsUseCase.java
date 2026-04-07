package com.hoaug.movieapi.modules.devicesession.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.devicesession.domain.repository.DeviceSessionRepository;

@Component
public class CountActiveDeviceSessionsUseCase {

  private final DeviceSessionRepository deviceSessionRepository;

  public CountActiveDeviceSessionsUseCase(DeviceSessionRepository deviceSessionRepository) {
    this.deviceSessionRepository = deviceSessionRepository;
  }

  public Long execute (Long userId) {
    return deviceSessionRepository.countByUserIdAndIsRevokedFalse(userId);
  }
}