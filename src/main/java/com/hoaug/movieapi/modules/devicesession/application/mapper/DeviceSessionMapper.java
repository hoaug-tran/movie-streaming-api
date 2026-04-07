package com.hoaug.movieapi.modules.devicesession.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.devicesession.application.dto.response.DeviceSessionResponse;
import com.hoaug.movieapi.modules.devicesession.domain.model.DeviceSession;

@Component
public class DeviceSessionMapper {

  public DeviceSessionResponse toResponse (DeviceSession deviceSession) {
    DeviceSessionResponse response = new DeviceSessionResponse();
    response.setId(deviceSession.getId());
    response.setDeviceName(deviceSession.getDeviceName());
    response.setDeviceType(deviceSession.getDeviceType());
    response.setIpAddress(deviceSession.getIpAddress());
    response.setUserAgent(deviceSession.getUserAgent());
    response.setLastActiveAt(deviceSession.getLastActiveAt());
    response.setCreatedAt(deviceSession.getCreatedAt());
    response.setIsRevoked(deviceSession.getIsRevoked());
    return response;
  }
}