package com.hoaug.movieapi.modules.subscription.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.subscription.application.dto.response.SubscriptionListResponse;
import com.hoaug.movieapi.modules.subscription.application.dto.response.UserSubscriptionResponse;
import com.hoaug.movieapi.modules.subscription.application.mapper.SubscriptionMapper;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;

@Component
public class GetMySubscriptionsUseCase {

  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionPlanRepository subscriptionPlanRepository;
  private final SubscriptionMapper subscriptionMapper;

  public GetMySubscriptionsUseCase(UserSubscriptionRepository userSubscriptionRepository,
      SubscriptionPlanRepository subscriptionPlanRepository,
      SubscriptionMapper subscriptionMapper) {
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.subscriptionPlanRepository = subscriptionPlanRepository;
    this.subscriptionMapper = subscriptionMapper;
  }

  public SubscriptionListResponse execute (Long userId) {
    List<UserSubscriptionResponse> items = userSubscriptionRepository
        .findByUserIdOrderByCreatedAtDesc(userId).stream().map(subscription -> {
          UserSubscriptionResponse response = subscriptionMapper.toResponse(subscription);
          subscriptionPlanRepository.findById(subscription.getPlanId()).ifPresent(plan -> {
            response.setPlan(subscriptionMapper.toResponse(plan));
          });
          return response;
        }).toList();
    return new SubscriptionListResponse(items);
  }
}