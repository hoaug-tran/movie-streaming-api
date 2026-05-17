package com.hoaug.movieapi.modules.streaming.application.usecase;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.entity.DeviceSessionEntity;
import com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.repository.JpaDeviceSessionRepository;
import com.hoaug.movieapi.modules.streaming.application.service.SubscriptionAccessService;

@Component
public class StartStreamSessionUseCase {

  private static final int STREAM_TTL_SECONDS = 60;

  private final JpaDeviceSessionRepository deviceSessionRepository;
  private final SubscriptionAccessService subscriptionAccessService;

  public StartStreamSessionUseCase (JpaDeviceSessionRepository deviceSessionRepository,
      SubscriptionAccessService subscriptionAccessService) {
    this.deviceSessionRepository = deviceSessionRepository;
    this.subscriptionAccessService = subscriptionAccessService;
  }

  @Transactional
  public Long execute (Long userId, String deviceName, String deviceType, String userAgent,
      String ipAddress) {
    LocalDateTime now = LocalDateTime.now();
    int maxDevices = subscriptionAccessService.getMaxDevices(userId);

    List<DeviceSessionEntity> active = deviceSessionRepository
        .findActiveStreamingSessionsOldestFirst(userId, now);

    while (active.size() >= maxDevices) {
      DeviceSessionEntity oldest = active.remove(0);
      oldest.setIsStreaming(false);
      deviceSessionRepository.save(oldest);
    }

    DeviceSessionEntity session = deviceSessionRepository
        .findFirstByUserIdAndDeviceNameAndDeviceTypeAndUserAgentAndIsRevokedFalseOrderByLastActiveAtDesc(
            userId, deviceName, deviceType, userAgent)
        .orElseGet(() -> {
          DeviceSessionEntity s = new DeviceSessionEntity();
          s.setUserId(userId);
          s.setDeviceName(deviceName);
          s.setDeviceType(deviceType);
          s.setUserAgent(userAgent);
          s.setIpAddress(ipAddress);
          s.setIsRevoked(false);
          return s;
        });

    session.setIsStreaming(true);
    session.setStreamExpiresAt(now.plusSeconds(STREAM_TTL_SECONDS));
    session.setLastActiveAt(now);
    return deviceSessionRepository.save(session).getId();
  }
}
