package com.hoaug.movieapi.modules.subscription.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionPlan;

public interface SubscriptionPlanRepository {

  Optional<SubscriptionPlan> findById (Long id);

  Optional<SubscriptionPlan> findByCode (String code);

  boolean existsByCode (String code);

  SubscriptionPlan save (SubscriptionPlan subscriptionPlan);

  List<SubscriptionPlan> findByIsActiveTrueOrderByPriceAsc ();
}