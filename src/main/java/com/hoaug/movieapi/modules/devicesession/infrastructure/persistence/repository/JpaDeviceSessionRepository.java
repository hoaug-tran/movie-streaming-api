package com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.entity.DeviceSessionEntity;

public interface JpaDeviceSessionRepository extends JpaRepository<DeviceSessionEntity, Long> {

  List<DeviceSessionEntity> findByUserIdOrderByLastActiveAtDesc (Long userId);

  List<DeviceSessionEntity> findByUserIdAndIsRevokedFalseOrderByLastActiveAtDesc (Long userId);

  Long countByUserIdAndIsRevokedFalse (Long userId);

  Optional<DeviceSessionEntity> findFirstByUserIdAndDeviceNameAndDeviceTypeAndUserAgentAndIsRevokedFalseOrderByLastActiveAtDesc (
      Long userId, String deviceName, String deviceType, String userAgent);

  void deleteByLastActiveAtBefore (LocalDateTime dateTime);

  @Query("SELECT s FROM DeviceSessionEntity s WHERE s.userId = :userId AND s.isStreaming = true AND s.streamExpiresAt > :now AND s.isRevoked = false ORDER BY s.lastActiveAt ASC")
  List<DeviceSessionEntity> findActiveStreamingSessionsOldestFirst (@Param("userId") Long userId, @Param("now") LocalDateTime now);

  @Query("SELECT COUNT(s) FROM DeviceSessionEntity s WHERE s.userId = :userId AND s.isStreaming = true AND s.streamExpiresAt > :now AND s.isRevoked = false")
  long countActiveStreamingSessions (@Param("userId") Long userId, @Param("now") LocalDateTime now);

  @Modifying
  @Query("UPDATE DeviceSessionEntity s SET s.isStreaming = false WHERE s.streamExpiresAt <= :now AND s.isStreaming = true")
  void expireStaleStreamingSessions (@Param("now") LocalDateTime now);
}