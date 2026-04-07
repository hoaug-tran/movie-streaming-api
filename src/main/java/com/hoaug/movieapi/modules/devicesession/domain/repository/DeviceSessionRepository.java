package com.hoaug.movieapi.modules.devicesession.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.devicesession.domain.model.DeviceSession;

public interface DeviceSessionRepository {

  Optional<DeviceSession> findById (Long id);

  DeviceSession save (DeviceSession deviceSession);

  List<DeviceSession> findByUserIdOrderByLastActiveAtDesc (Long userId);

  List<DeviceSession> findByUserIdAndIsRevokedFalseOrderByLastActiveAtDesc (Long userId);

  Long countByUserIdAndIsRevokedFalse (Long userId);

  void deleteExpiredSessions (LocalDateTime dateTime);
}