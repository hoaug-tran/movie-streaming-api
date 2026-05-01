package com.hoaug.movieapi.modules.subscription.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.subscription.application.dto.request.UpdateAutoRenewRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.response.UserSubscriptionResponse;
import com.hoaug.movieapi.modules.subscription.application.mapper.SubscriptionMapper;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;

@Component
public class UpdateMyAutoRenewUseCase {
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionPlanRepository subscriptionPlanRepository;
  private final SubscriptionMapper subscriptionMapper;

  public UpdateMyAutoRenewUseCase(UserSubscriptionRepository userSubscriptionRepository,
      SubscriptionPlanRepository subscriptionPlanRepository, SubscriptionMapper subscriptionMapper) {
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.subscriptionPlanRepository = subscriptionPlanRepository;
    this.subscriptionMapper = subscriptionMapper;
  }

  public UserSubscriptionResponse execute (Long userId, UpdateAutoRenewRequest request) {
    UserSubscription subscription = userSubscriptionRepository.findByUserIdOrderByCreatedAtDesc(userId)
        .stream().filter(item -> item.getStatus() == SubscriptionStatus.ACTIVE).findFirst()
        .orElseThrow( () -> new AppException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND));
    subscription.setAutoRenew(request.getAutoRenew());
    UserSubscription saved = userSubscriptionRepository.save(subscription);
    UserSubscriptionResponse response = subscriptionMapper.toResponse(saved);
    subscriptionPlanRepository.findById(saved.getPlanId())
        .ifPresent(plan -> response.setPlan(subscriptionMapper.toResponse(plan)));
    return response;
  }
}
