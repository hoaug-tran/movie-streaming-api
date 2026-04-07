package com.hoaug.movieapi.modules.devicesession.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.devicesession.application.dto.request.CreateDeviceSessionRequest;
import com.hoaug.movieapi.modules.devicesession.application.dto.response.DeviceSessionResponse;
import com.hoaug.movieapi.modules.devicesession.application.mapper.DeviceSessionMapper;
import com.hoaug.movieapi.modules.devicesession.domain.model.DeviceSession;
import com.hoaug.movieapi.modules.devicesession.domain.repository.DeviceSessionRepository;

@Component
public class CreateDeviceSessionUseCase {

  private final DeviceSessionRepository deviceSessionRepository;
  private final DeviceSessionMapper deviceSessionMapper;

  public CreateDeviceSessionUseCase(DeviceSessionRepository deviceSessionRepository,
      DeviceSessionMapper deviceSessionMapper) {
    this.deviceSessionRepository = deviceSessionRepository;
    this.deviceSessionMapper = deviceSessionMapper;
  }

  public DeviceSessionResponse execute (Long userId, CreateDeviceSessionRequest request) {
    DeviceSession deviceSession = new DeviceSession();
    deviceSession.setUserId(userId);
    deviceSession.setDeviceName(request.getDeviceName());
    deviceSession.setDeviceType(request.getDeviceType());
    deviceSession.setIpAddress(request.getIpAddress());
    deviceSession.setUserAgent(request.getUserAgent());
    deviceSession.setLastActiveAt(LocalDateTime.now());
    deviceSession.setCreatedAt(LocalDateTime.now());
    deviceSession.setIsRevoked(false);

    DeviceSession savedDeviceSession = deviceSessionRepository.save(deviceSession);
    return deviceSessionMapper.toResponse(savedDeviceSession);
  }
}