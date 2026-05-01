package com.hoaug.movieapi.modules.subscription.application.usecase;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.subscription.application.dto.response.UserSubscriptionResponse;
import com.hoaug.movieapi.modules.subscription.application.mapper.SubscriptionMapper;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;

@Component
public class GetMyCurrentSubscriptionUseCase {
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionPlanRepository subscriptionPlanRepository;
  private final SubscriptionMapper subscriptionMapper;

  public GetMyCurrentSubscriptionUseCase(UserSubscriptionRepository userSubscriptionRepository,
      SubscriptionPlanRepository subscriptionPlanRepository, SubscriptionMapper subscriptionMapper) {
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.subscriptionPlanRepository = subscriptionPlanRepository;
    this.subscriptionMapper = subscriptionMapper;
  }

  public Optional<UserSubscriptionResponse> execute (Long userId) {
    LocalDateTime now = LocalDateTime.now();
    return userSubscriptionRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
        .filter(subscription -> subscription.getStatus() == SubscriptionStatus.ACTIVE)
        .filter(subscription -> subscription.getEndAt() == null || subscription.getEndAt().isAfter(now))
        .findFirst().map(this::toResponse);
  }

  private UserSubscriptionResponse toResponse (UserSubscription subscription) {
    UserSubscriptionResponse response = subscriptionMapper.toResponse(subscription);
    subscriptionPlanRepository.findById(subscription.getPlanId())
        .ifPresent(plan -> response.setPlan(subscriptionMapper.toResponse(plan)));
    if (subscription.getEndAt() != null) {
      response.setRemainingSeconds(Math.max(0,
          java.time.Duration.between(LocalDateTime.now(), subscription.getEndAt()).getSeconds()));
    }
    return response;
  }
}
