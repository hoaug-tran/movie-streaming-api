package com.hoaug.movieapi.modules.subscription.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.subscription.application.dto.response.SubscriptionPlanResponse;
import com.hoaug.movieapi.modules.subscription.application.mapper.SubscriptionMapper;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;

@Component
public class GetActiveSubscriptionPlansUseCase {

  private final SubscriptionPlanRepository subscriptionPlanRepository;
  private final SubscriptionMapper subscriptionMapper;

  public GetActiveSubscriptionPlansUseCase(SubscriptionPlanRepository subscriptionPlanRepository,
      SubscriptionMapper subscriptionMapper) {
    this.subscriptionPlanRepository = subscriptionPlanRepository;
    this.subscriptionMapper = subscriptionMapper;
  }

  public List<SubscriptionPlanResponse> execute () {
    return subscriptionPlanRepository.findByIsActiveTrueOrderByPriceAsc().stream()
        .map(subscriptionMapper::toResponse).toList();
  }
}