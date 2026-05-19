package com.hoaug.movieapi.modules.subscription.application.usecase;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;

@Component
public class DeleteSubscriptionPlanUseCase {

  private final SubscriptionPlanRepository subscriptionPlanRepository;

  public DeleteSubscriptionPlanUseCase(SubscriptionPlanRepository subscriptionPlanRepository) {
    this.subscriptionPlanRepository = subscriptionPlanRepository;
  }

  @CacheEvict(cacheNames = "subscriptionPlans", allEntries = true)
  public void execute (Long planId) {
    subscriptionPlanRepository.findById(planId)
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));
    subscriptionPlanRepository.deleteById(planId);
  }
}
