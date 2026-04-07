package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity.SubscriptionPlanEntity;

public interface JpaSubscriptionPlanRepository extends JpaRepository<SubscriptionPlanEntity, Long> {

  Optional<SubscriptionPlanEntity> findByCode (String code);

  boolean existsByCode (String code);

  List<SubscriptionPlanEntity> findByIsActiveTrueOrderByPriceAsc ();
}