package com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.entity.DeviceSessionEntity;

public interface JpaDeviceSessionRepository extends JpaRepository<DeviceSessionEntity, Long> {

  List<DeviceSessionEntity> findByUserIdOrderByLastActiveAtDesc (Long userId);

  List<DeviceSessionEntity> findByUserIdAndIsRevokedFalseOrderByLastActiveAtDesc (Long userId);

  Long countByUserIdAndIsRevokedFalse (Long userId);

  Optional<DeviceSessionEntity> findFirstByUserIdAndDeviceNameAndDeviceTypeAndUserAgentAndIsRevokedFalseOrderByLastActiveAtDesc (
      Long userId, String deviceName, String deviceType, String userAgent);

  void deleteByLastActiveAtBefore (LocalDateTime dateTime);
}