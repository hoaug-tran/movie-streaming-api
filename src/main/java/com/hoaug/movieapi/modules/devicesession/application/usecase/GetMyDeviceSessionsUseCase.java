package com.hoaug.movieapi.modules.devicesession.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.devicesession.application.dto.response.DeviceSessionResponse;
import com.hoaug.movieapi.modules.devicesession.application.mapper.DeviceSessionMapper;
import com.hoaug.movieapi.modules.devicesession.domain.repository.DeviceSessionRepository;

@Component
public class GetMyDeviceSessionsUseCase {

  private final DeviceSessionRepository deviceSessionRepository;
  private final DeviceSessionMapper deviceSessionMapper;

  public GetMyDeviceSessionsUseCase(DeviceSessionRepository deviceSessionRepository,
      DeviceSessionMapper deviceSessionMapper) {
    this.deviceSessionRepository = deviceSessionRepository;
    this.deviceSessionMapper = deviceSessionMapper;
  }

  public List<DeviceSessionResponse> execute (Long userId) {
    return deviceSessionRepository.findByUserIdOrderByLastActiveAtDesc(userId).stream()
        .map(deviceSessionMapper::toResponse).toList();
  }
}