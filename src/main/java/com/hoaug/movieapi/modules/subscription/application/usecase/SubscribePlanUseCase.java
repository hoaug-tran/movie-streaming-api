package com.hoaug.movieapi.modules.subscription.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.activitylog.application.service.ActivityLogService;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivityScope;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivitySeverity;
import com.hoaug.movieapi.modules.subscription.application.dto.request.SubscribePlanRequest;
import com.hoaug.movieapi.modules.subscription.application.dto.response.UserSubscriptionResponse;
import com.hoaug.movieapi.modules.subscription.application.mapper.SubscriptionMapper;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionPlan;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class SubscribePlanUseCase {

  private final SubscriptionPlanRepository subscriptionPlanRepository;
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionMapper subscriptionMapper;
  private final UserRepository userRepository;
  private final ActivityLogService activityLogService;

  public SubscribePlanUseCase(SubscriptionPlanRepository subscriptionPlanRepository,
      UserSubscriptionRepository userSubscriptionRepository, SubscriptionMapper subscriptionMapper,
      UserRepository userRepository, ActivityLogService activityLogService) {
    this.subscriptionPlanRepository = subscriptionPlanRepository;
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.subscriptionMapper = subscriptionMapper;
    this.userRepository = userRepository;
    this.activityLogService = activityLogService;
  }

  public UserSubscriptionResponse execute (Long userId, SubscribePlanRequest request) {
    SubscriptionPlan plan = subscriptionPlanRepository.findById(request.getPlanId())
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

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

  @Transactional
  public UserSubscriptionResponse executeForUser (Long userId, SubscribePlanRequest request) {
    SubscriptionPlan plan = subscriptionPlanRepository.findById(request.getPlanId())
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

    if (!Boolean.TRUE.equals(plan.getIsActive())) {
      throw new AppException(ErrorCode.SUBSCRIPTION_PLAN_INACTIVE);
    }

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expiryDate = now.plusDays(plan.getDurationDays());

    userSubscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
        .forEach(oldSub -> {
          oldSub.setStatus(SubscriptionStatus.EXPIRED);
          oldSub.setUpdatedAt(now);
          userSubscriptionRepository.save(oldSub);
        });

    UserSubscription subscription = new UserSubscription();
    subscription.setUserId(userId);
    subscription.setPlanId(plan.getId());
    subscription.setStartAt(now);
    subscription.setEndAt(expiryDate);
    subscription.setStatus(SubscriptionStatus.ACTIVE);
    subscription.setAutoRenew(request.getAutoRenew() != null ? request.getAutoRenew() : false);
    subscription.setCreatedAt(now);
    subscription.setUpdatedAt(now);

    UserSubscription saved = userSubscriptionRepository.save(subscription);

    userRepository.updatePremiumExpiryDate(userId, expiryDate);

    userRepository.findById(userId).ifPresent(user -> {
      activityLogService.record(ActivityScope.USER, user.getId(), user.getFullName(),
          "Kích hoạt VIP", "SUBSCRIPTION", saved.getId(), plan.getName(),
          user.getFullName() + " được kích hoạt trực tiếp gói VIP " + plan.getName() + ".",
          ActivitySeverity.SUCCESS);
    });

    return subscriptionMapper.toResponse(saved);
  }

}
