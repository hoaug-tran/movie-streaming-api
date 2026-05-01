package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity.UserSubscriptionEntity;

public interface JpaUserSubscriptionRepository extends JpaRepository<UserSubscriptionEntity, Long> {

  List<UserSubscriptionEntity> findByUserIdOrderByCreatedAtDesc (Long userId);

  List<UserSubscriptionEntity> findByStatusAndEndAtBefore (SubscriptionStatus status,
      LocalDateTime endAt);

  List<UserSubscriptionEntity> findByStatusAndEndAtBetween (SubscriptionStatus status,
      LocalDateTime startDate, LocalDateTime endDate);

  java.util.Optional<UserSubscriptionEntity> findFirstByUserIdAndStatusOrderByEndAtDesc(
      Long userId, SubscriptionStatus status);

  @org.springframework.data.jpa.repository.Query(
      "SELECT us FROM UserSubscriptionEntity us WHERE us.userId = :userId AND us.status = 'ACTIVE' AND us.endAt > CURRENT_TIMESTAMP")
  java.util.Optional<UserSubscriptionEntity> findActiveByUserId(
      @org.springframework.data.repository.query.Param("userId") Long userId);
}