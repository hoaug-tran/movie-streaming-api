package com.hoaug.movieapi.modules.subscription.application.usecase;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.subscription.application.dto.request.SubscribePlanRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.response.UserSubscriptionResponse;
import com.hoaug.movieapi.modules.subscription.application.mapper.SubscriptionMapper;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionPlan;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;

@Component
public class SubscribePlanUseCase {

  private final SubscriptionPlanRepository subscriptionPlanRepository;
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionMapper subscriptionMapper;

  public SubscribePlanUseCase(SubscriptionPlanRepository subscriptionPlanRepository,
      UserSubscriptionRepository userSubscriptionRepository,
      SubscriptionMapper subscriptionMapper) {
    this.subscriptionPlanRepository = subscriptionPlanRepository;
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.subscriptionMapper = subscriptionMapper;
  }

  public UserSubscriptionResponse execute(Long userId, SubscribePlanRequest request) {
    SubscriptionPlan plan = subscriptionPlanRepository.findById(request.getPlanId())
        .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

    if (!Boolean.TRUE.equals(plan.getIsActive())) {
      throw new AppException(ErrorCode.SUBSCRIPTION_PLAN_INACTIVE);
    }

    LocalDateTime now = LocalDateTime.now();

    UserSubscription subscription = new UserSubscription();
    subscription.setUserId(userId);
    subscription.setPlanId(plan.getId());
    subscription.setStartAt(now);
    subscription.setEndAt(now.plusDays(plan.getDurationDays()));
    subscription.setStatus(SubscriptionStatus.PENDING);
    subscription.setAutoRenew(request.getAutoRenew());
    subscription.setCreatedAt(now);
    subscription.setUpdatedAt(now);

    return subscriptionMapper.toResponse(userSubscriptionRepository.save(subscription));
  }
}
