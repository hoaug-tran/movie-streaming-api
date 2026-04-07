package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionPlan;
import com.hoaug.movieapi.modules.subscription.domain.repository.SubscriptionPlanRepository;
import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity.SubscriptionPlanEntity;
import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.repository.JpaSubscriptionPlanRepository;

@Component
public class SubscriptionPlanRepositoryAdapter implements SubscriptionPlanRepository {

  private final JpaSubscriptionPlanRepository jpaSubscriptionPlanRepository;

  public SubscriptionPlanRepositoryAdapter(
      JpaSubscriptionPlanRepository jpaSubscriptionPlanRepository) {
    this.jpaSubscriptionPlanRepository = jpaSubscriptionPlanRepository;
  }

  @Override
  public Optional<SubscriptionPlan> findById (Long id) {
    return jpaSubscriptionPlanRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<SubscriptionPlan> findByCode (String code) {
    return jpaSubscriptionPlanRepository.findByCode(code).map(this::toDomain);
  }

  @Override
  public boolean existsByCode (String code) {
    return jpaSubscriptionPlanRepository.existsByCode(code);
  }

  @Override
  public SubscriptionPlan save (SubscriptionPlan subscriptionPlan) {
    return toDomain(jpaSubscriptionPlanRepository.save(toEntity(subscriptionPlan)));
  }

  @Override
  public List<SubscriptionPlan> findByIsActiveTrueOrderByPriceAsc () {
    return jpaSubscriptionPlanRepository.findByIsActiveTrueOrderByPriceAsc().stream()
        .map(this::toDomain).toList();
  }

  private SubscriptionPlan toDomain (SubscriptionPlanEntity entity) {
    SubscriptionPlan plan = new SubscriptionPlan();
    plan.setId(entity.getId());
    plan.setName(entity.getName());
    plan.setCode(entity.getCode());
    plan.setDescription(entity.getDescription());
    plan.setPrice(entity.getPrice());
    plan.setDurationDays(entity.getDurationDays());
    plan.setMaxDevices(entity.getMaxDevices());
    plan.setVideoQuality(entity.getVideoQuality());
    plan.setHasAdsFree(entity.getHasAdsFree());
    plan.setIsActive(entity.getIsActive());
    plan.setCreatedAt(entity.getCreatedAt());
    plan.setUpdatedAt(entity.getUpdatedAt());
    return plan;
  }

  private SubscriptionPlanEntity toEntity (SubscriptionPlan plan) {
    SubscriptionPlanEntity entity = new SubscriptionPlanEntity();
    entity.setId(plan.getId());
    entity.setName(plan.getName());
    entity.setCode(plan.getCode());
    entity.setDescription(plan.getDescription());
    entity.setPrice(plan.getPrice());
    entity.setDurationDays(plan.getDurationDays());
    entity.setMaxDevices(plan.getMaxDevices());
    entity.setVideoQuality(plan.getVideoQuality());
    entity.setHasAdsFree(plan.getHasAdsFree());
    entity.setIsActive(plan.getIsActive());
    entity.setCreatedAt(plan.getCreatedAt());
    entity.setUpdatedAt(plan.getUpdatedAt());
    return entity;
  }
}