package com.hoaug.movieapi.modules.user.application.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionPlan;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;
import com.hoaug.movieapi.modules.user.application.dto.request.AdminUpdateUserRequest;
import com.hoaug.movieapi.modules.user.application.dto.response.UserDetailResponse;
import com.hoaug.movieapi.modules.user.application.mapper.UserMapper;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class AdminUpdateUserUseCase {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final SubscriptionPlanRepository subscriptionPlanRepository;

  public AdminUpdateUserUseCase(UserRepository userRepository, UserMapper userMapper,
      UserSubscriptionRepository userSubscriptionRepository,
      SubscriptionPlanRepository subscriptionPlanRepository) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.subscriptionPlanRepository = subscriptionPlanRepository;
  }

  @Transactional
  public UserDetailResponse execute (Long userId, AdminUpdateUserRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    userRepository.findByEmail(request.getEmail())
        .filter(existing -> !existing.getId().equals(userId)).ifPresent(existing -> {
          throw new AppException(ErrorCode.EMAIL_EXISTED);
        });

    user.setEmail(request.getEmail());
    user.setFullName(request.getFullName());
    user.setAvatarUrl(request.getAvatarUrl());
    user.setRole(request.getRole());
    user.setAccountStatus(request.getAccountStatus());

    if (request.getPremiumExpiryDate() != null) {
      user.setPremiumExpiryDate(request.getPremiumExpiryDate());
    }

    User savedUser = userRepository.save(user);

    if (request.getPremiumExpiryDate() != null) {
      syncAdminGrantedSubscription(userId, request.getPremiumExpiryDate());
    }

    return userMapper.toDetailResponse(savedUser);
  }

  private void syncAdminGrantedSubscription (Long userId,
      java.time.LocalDateTime premiumExpiryDate) {
    if (premiumExpiryDate == null || !premiumExpiryDate.isAfter(java.time.LocalDateTime.now()))
      return;

    Long planId = userSubscriptionRepository.findActiveByUserId(userId)
        .map(UserSubscription::getPlanId).orElseGet(this::resolveDefaultAdminGrantPlanId);

    UserSubscription subscription = userSubscriptionRepository.findActiveByUserId(userId)
        .orElseGet(UserSubscription::new);
    subscription.setUserId(userId);
    subscription.setPlanId(planId);
    subscription.setStartAt(subscription.getStartAt() != null ? subscription.getStartAt()
        : java.time.LocalDateTime.now());
    subscription.setEndAt(premiumExpiryDate);
    subscription.setStatus(SubscriptionStatus.ACTIVE);
    subscription.setAutoRenew(false);
    userSubscriptionRepository.save(subscription);
  }

  private Long resolveDefaultAdminGrantPlanId () {
    return subscriptionPlanRepository.findByCode("PREMIUM_PLUS")
        .or( () -> subscriptionPlanRepository.findByCode("PREMIUM"))
        .or( () -> subscriptionPlanRepository.findByIsActiveTrueOrderByPriceAsc().stream()
            .reduce( (first, second) -> second))
        .map(SubscriptionPlan::getId)
        .orElseThrow( () -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));
  }
}
