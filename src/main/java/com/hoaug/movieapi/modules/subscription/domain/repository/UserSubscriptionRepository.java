package com.hoaug.movieapi.modules.subscription.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;

public interface UserSubscriptionRepository {

  Optional<UserSubscription> findById (Long id);

  UserSubscription save (UserSubscription userSubscription);

  List<UserSubscription> findByUserIdOrderByCreatedAtDesc (Long userId);

  List<UserSubscription> findByStatusAndEndAtBefore (SubscriptionStatus status,
      LocalDateTime dateTime);

  List<UserSubscription> findByStatusAndEndAtBetween (SubscriptionStatus status,
      LocalDateTime startDate, LocalDateTime endDate);

  Optional<UserSubscription> findFirstByUserIdAndStatusOrderByEndAtDesc (Long userId,
      SubscriptionStatus status);
}