package com.hoaug.movieapi.modules.subscription.application.usecase;

import java.time.LocalDateTime;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.subscription.application.dto.request.UpdateSubscriptionPlanRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.response.SubscriptionPlanResponse;
import com.hoaug.movieapi.modules.subscription.application.mapper.SubscriptionMapper;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionPlan;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;

@Component
public class UpdateSubscriptionPlanUseCase {

  private final SubscriptionPlanRepository subscriptionPlanRepository;
  private final SubscriptionMapper subscriptionMapper;

  public UpdateSubscriptionPlanUseCase(SubscriptionPlanRepository subscriptionPlanRepository,
      SubscriptionMapper subscriptionMapper) {
    this.subscriptionPlanRepository = subscriptionPlanRepository;
    this.subscriptionMapper = subscriptionMapper;
  }

  @CacheEvict(cacheNames = "subscriptionPlans", allEntries = true)
  public SubscriptionPlanResponse execute (Long planId, UpdateSubscriptionPlanRequest request) {
    SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

    subscriptionPlanRepository.findByCode(request.getCode())
        .filter(existing -> !existing.getId().equals(planId))
        .ifPresent(existing -> {
          throw new AppException(ErrorCode.SUBSCRIPTION_PLAN_CODE_EXISTS);
        });

    plan.setName(request.getName());
    plan.setCode(request.getCode());
    plan.setDescription(request.getDescription());
    plan.setPrice(request.getPrice());
    plan.setDurationDays(request.getDurationDays());
    plan.setMaxDevices(request.getMaxDevices());
    plan.setVideoQuality(request.getVideoQuality());
    plan.setHasAdsFree(request.getHasAdsFree());
    plan.setIsActive(request.getIsActive());
    plan.setUpdatedAt(LocalDateTime.now());

    return subscriptionMapper.toResponse(subscriptionPlanRepository.save(plan));
  }
}
