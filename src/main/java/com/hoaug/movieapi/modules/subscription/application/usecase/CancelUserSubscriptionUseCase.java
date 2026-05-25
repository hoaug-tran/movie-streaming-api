package com.hoaug.movieapi.modules.subscription.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class CancelUserSubscriptionUseCase {

  private final UserSubscriptionRepository userSubscriptionRepository;
  private final UserRepository userRepository;

  public CancelUserSubscriptionUseCase(UserSubscriptionRepository userSubscriptionRepository,
      UserRepository userRepository) {
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public void execute (Long userId) {
    LocalDateTime now = LocalDateTime.now();

    userSubscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
        .forEach(sub -> {
          sub.setStatus(SubscriptionStatus.CANCELLED);
          sub.setEndAt(now);
          sub.setUpdatedAt(now);
          userSubscriptionRepository.save(sub);
        });

    userRepository.updatePremiumExpiryDate(userId, null);
  }
}