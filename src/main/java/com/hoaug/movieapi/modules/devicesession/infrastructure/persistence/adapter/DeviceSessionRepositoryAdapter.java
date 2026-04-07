package com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.devicesession.domain.model.DeviceSession;
import com.hoaug.movieapi.modules.devicesession.domain.repository.DeviceSessionRepository;
import com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.entity.DeviceSessionEntity;
import com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.repository.JpaDeviceSessionRepository;

@Component
public class DeviceSessionRepositoryAdapter implements DeviceSessionRepository {

  private final JpaDeviceSessionRepository jpaDeviceSessionRepository;

  public DeviceSessionRepositoryAdapter(JpaDeviceSessionRepository jpaDeviceSessionRepository) {
    this.jpaDeviceSessionRepository = jpaDeviceSessionRepository;
  }

  @Override
  public Optional<DeviceSession> findById (Long id) {
    return jpaDeviceSessionRepository.findById(id).map(this::toDomain);
  }

  @Override
  public DeviceSession save (DeviceSession deviceSession) {
    DeviceSessionEntity savedEntity = jpaDeviceSessionRepository.save(toEntity(deviceSession));
    return toDomain(savedEntity);
  }

  @Override
  public List<DeviceSession> findByUserIdOrderByLastActiveAtDesc (Long userId) {
    return jpaDeviceSessionRepository.findByUserIdOrderByLastActiveAtDesc(userId).stream()
        .map(this::toDomain).toList();
  }

  @Override
  public List<DeviceSession> findByUserIdAndIsRevokedFalseOrderByLastActiveAtDesc (Long userId) {
    return jpaDeviceSessionRepository.findByUserIdAndIsRevokedFalseOrderByLastActiveAtDesc(userId)
        .stream().map(this::toDomain).toList();
  }

  @Override
  public Long countByUserIdAndIsRevokedFalse (Long userId) {
    return jpaDeviceSessionRepository.countByUserIdAndIsRevokedFalse(userId);
  }

  @Override
  public void deleteExpiredSessions (LocalDateTime dateTime) {
    jpaDeviceSessionRepository.deleteByLastActiveAtBefore(dateTime);
  }

  private DeviceSession toDomain (DeviceSessionEntity entity) {
    DeviceSession deviceSession = new DeviceSession();
    deviceSession.setId(entity.getId());
    deviceSession.setUserId(entity.getUserId());
    deviceSession.setDeviceName(entity.getDeviceName());
    deviceSession.setDeviceType(entity.getDeviceType());
    deviceSession.setIpAddress(entity.getIpAddress());
    deviceSession.setUserAgent(entity.getUserAgent());
    deviceSession.setLastActiveAt(entity.getLastActiveAt());
    deviceSession.setCreatedAt(entity.getCreatedAt());
    deviceSession.setIsRevoked(entity.getIsRevoked());
    return deviceSession;
  }

  private DeviceSessionEntity toEntity (DeviceSession deviceSession) {
    DeviceSessionEntity entity = new DeviceSessionEntity();
    entity.setId(deviceSession.getId());
    entity.setUserId(deviceSession.getUserId());
    entity.setDeviceName(deviceSession.getDeviceName());
    entity.setDeviceType(deviceSession.getDeviceType());
    entity.setIpAddress(deviceSession.getIpAddress());
    entity.setUserAgent(deviceSession.getUserAgent());
    entity.setLastActiveAt(deviceSession.getLastActiveAt());
    entity.setCreatedAt(deviceSession.getCreatedAt());
    entity.setIsRevoked(deviceSession.getIsRevoked());
    return entity;
  }
}