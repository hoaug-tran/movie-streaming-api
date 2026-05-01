package com.hoaug.movieapi.modules.devicesession.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.devicesession.application.dto.request.CreateDeviceSessionRequest;
import com.hoaug.movieapi.modules.devicesession.application.dto.response.DeviceSessionResponse;
import com.hoaug.movieapi.modules.devicesession.application.mapper.DeviceSessionMapper;
import com.hoaug.movieapi.modules.devicesession.domain.model.DeviceSession;
import com.hoaug.movieapi.modules.devicesession.domain.repository.DeviceSessionRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreateDeviceSessionUseCase {

  private final DeviceSessionRepository deviceSessionRepository;
  private final DeviceSessionMapper deviceSessionMapper;
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionPlanRepository subscriptionPlanRepository;

  public CreateDeviceSessionUseCase(DeviceSessionRepository deviceSessionRepository,
      DeviceSessionMapper deviceSessionMapper,
      UserSubscriptionRepository userSubscriptionRepository,
      SubscriptionPlanRepository subscriptionPlanRepository) {
    this.deviceSessionRepository = deviceSessionRepository;
    this.deviceSessionMapper = deviceSessionMapper;
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.subscriptionPlanRepository = subscriptionPlanRepository;
  }

  public DeviceSessionResponse execute (Long userId, CreateDeviceSessionRequest request) {
    int maxDevices = userSubscriptionRepository.findActiveByUserId(userId)
        .flatMap(sub -> subscriptionPlanRepository.findById(sub.getPlanId()))
        .map(plan -> plan.getMaxDevices()).orElse(1);

    var activeSessions =
        deviceSessionRepository.findByUserIdAndIsRevokedFalseOrderByLastActiveAtDesc(userId);

    if (activeSessions.size() >= maxDevices) {
      DeviceSession oldestSession = activeSessions.get(activeSessions.size() - 1);
      oldestSession.setIsRevoked(true);
      deviceSessionRepository.save(oldestSession);

      log.info(
          "Auto-revoked oldest device session: userId={}, deviceId={}, deviceName={}, maxDevices={}",
          userId, oldestSession.getId(), oldestSession.getDeviceName(), maxDevices);
    }

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