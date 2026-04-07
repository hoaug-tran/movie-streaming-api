package com.hoaug.movieapi.modules.subscription.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;

public interface UserSubscriptionRepository {

  Optional<UserSubscription> findById (Long id);

  UserSubscription save (UserSubscription userSubscription);

  List<UserSubscription> findByUserIdOrderByCreatedAtDesc (Long userId);
}