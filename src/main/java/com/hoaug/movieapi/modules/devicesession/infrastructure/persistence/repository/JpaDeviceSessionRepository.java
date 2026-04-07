package com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.entity.DeviceSessionEntity;

public interface JpaDeviceSessionRepository extends JpaRepository<DeviceSessionEntity, Long> {

  List<DeviceSessionEntity> findByUserIdOrderByLastActiveAtDesc (Long userId);

  List<DeviceSessionEntity> findByUserIdAndIsRevokedFalseOrderByLastActiveAtDesc (Long userId);

  Long countByUserIdAndIsRevokedFalse (Long userId);

  void deleteByLastActiveAtBefore (LocalDateTime dateTime);
}