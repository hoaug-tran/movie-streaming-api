package com.hoaug.movieapi.modules.streaming.application.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionPlan;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;

@Component
public class SubscriptionAccessService {

  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionPlanRepository subscriptionPlanRepository;

  public SubscriptionAccessService(UserSubscriptionRepository userSubscriptionRepository,
      SubscriptionPlanRepository subscriptionPlanRepository) {
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.subscriptionPlanRepository = subscriptionPlanRepository;
  }

  public boolean canAccessQuality (Long userId, String quality) {
    if ("720p".equals(quality))
      return true;

    return getActivePlan(userId).map(plan -> switch (quality) {
    case "1080p" -> "PREMIUM".equals(plan.getCode()) || "PREMIUM_PLUS".equals(plan.getCode());
    case "4K" -> "PREMIUM_PLUS".equals(plan.getCode());
    default -> false;
    }).orElse(false);
  }

  public boolean canDownloadOffline (Long userId) {
    return getActivePlan(userId).map(plan -> "PREMIUM_PLUS".equals(plan.getCode())).orElse(false);
  }

  public int getMaxDevices (Long userId) {
    return getActivePlan(userId).map(p -> p.getMaxDevices() != null ? p.getMaxDevices() : 1)
        .orElse(1);
  }

  private Optional<SubscriptionPlan> getActivePlan (Long userId) {
    LocalDateTime now = LocalDateTime.now();
    return userSubscriptionRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
        .filter(s -> s.getStatus() == SubscriptionStatus.ACTIVE)
        .filter(s -> s.getEndAt() == null || s.getEndAt().isAfter(now)).findFirst()
        .flatMap(s -> subscriptionPlanRepository.findById(s.getPlanId()));
  }
}
