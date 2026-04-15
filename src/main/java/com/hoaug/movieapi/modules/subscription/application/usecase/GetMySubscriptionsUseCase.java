package com.hoaug.movieapi.modules.subscription.application.usecase;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.subscription.application.dto.response.UserSubscriptionResponse;
import com.hoaug.movieapi.modules.subscription.application.mapper.SubscriptionMapper;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;

@Component
public class GetMySubscriptionsUseCase {

  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionMapper subscriptionMapper;

  public GetMySubscriptionsUseCase(UserSubscriptionRepository userSubscriptionRepository,
      SubscriptionMapper subscriptionMapper) {
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.subscriptionMapper = subscriptionMapper;
  }

  @Cacheable(cacheNames = "userSubscription", key = "'user:' + #userId + ':subscriptions'")
  public List<UserSubscriptionResponse> execute (Long userId) {
    return userSubscriptionRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
        .map(subscriptionMapper::toResponse).toList();
  }
}