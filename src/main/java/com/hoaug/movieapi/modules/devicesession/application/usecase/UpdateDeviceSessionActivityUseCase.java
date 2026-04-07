package com.hoaug.movieapi.modules.devicesession.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.devicesession.application.dto.response.DeviceSessionResponse;
import com.hoaug.movieapi.modules.devicesession.application.mapper.DeviceSessionMapper;
import com.hoaug.movieapi.modules.devicesession.domain.model.DeviceSession;
import com.hoaug.movieapi.modules.devicesession.domain.repository.DeviceSessionRepository;

@Component
public class UpdateDeviceSessionActivityUseCase {

  private final DeviceSessionRepository deviceSessionRepository;
  private final DeviceSessionMapper deviceSessionMapper;

  public UpdateDeviceSessionActivityUseCase(DeviceSessionRepository deviceSessionRepository,
      DeviceSessionMapper deviceSessionMapper) {
    this.deviceSessionRepository = deviceSessionRepository;
    this.deviceSessionMapper = deviceSessionMapper;
  }

  public DeviceSessionResponse execute (Long userId, Long sessionId) {
    DeviceSession deviceSession = deviceSessionRepository.findById(sessionId)
        .orElseThrow( () -> new AppException(ErrorCode.DEVICE_SESSION_NOT_FOUND));

    if (!deviceSession.getUserId().equals(userId)) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    deviceSession.setLastActiveAt(LocalDateTime.now());

    DeviceSession savedDeviceSession = deviceSessionRepository.save(deviceSession);
    return deviceSessionMapper.toResponse(savedDeviceSession);
  }
}