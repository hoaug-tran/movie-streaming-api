package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity.UserSubscriptionEntity;

public interface JpaUserSubscriptionRepository extends JpaRepository<UserSubscriptionEntity, Long> {

  interface PlanRevenueProjection {
    Long getPlanId ();

    String getPlanName ();

    String getPlanCode ();

    BigDecimal getRevenue ();

    Long getSubscriptions ();
  }

  List<UserSubscriptionEntity> findByUserIdOrderByCreatedAtDesc (Long userId);

  List<UserSubscriptionEntity> findByStatusAndEndAtBefore (SubscriptionStatus status,
      LocalDateTime endAt);

  List<UserSubscriptionEntity> findByStatusAndEndAtBetween (SubscriptionStatus status,
      LocalDateTime startDate, LocalDateTime endDate);

  List<UserSubscriptionEntity> findByStatusAndCreatedAtBefore (SubscriptionStatus status,
      LocalDateTime createdAt);

  java.util.Optional<UserSubscriptionEntity> findFirstByUserIdAndStatusOrderByEndAtDesc(
      Long userId, SubscriptionStatus status);

  @org.springframework.data.jpa.repository.Query(
      "SELECT us FROM UserSubscriptionEntity us WHERE us.userId = :userId AND us.status = 'ACTIVE' AND us.endAt > CURRENT_TIMESTAMP")
  java.util.Optional<UserSubscriptionEntity> findActiveByUserId(
      @org.springframework.data.repository.query.Param("userId") Long userId);

  @org.springframework.data.jpa.repository.Query(value = """
      SELECT sp.id AS planId,
             sp.name AS planName,
             sp.code AS planCode,
             COALESCE(SUM(pt.amount), 0) AS revenue,
             COUNT(DISTINCT us.id) AS subscriptions
      FROM subscription_plans sp
      LEFT JOIN user_subscriptions us ON us.plan_id = sp.id
      LEFT JOIN payment_transactions pt ON pt.subscription_id = us.id AND pt.status = 'SUCCESS'
      GROUP BY sp.id, sp.name, sp.code
      ORDER BY revenue DESC, subscriptions DESC, sp.id DESC
      """, nativeQuery = true)
  List<PlanRevenueProjection> findTopPlanRevenue(org.springframework.data.domain.Pageable pageable);

  List<UserSubscriptionEntity> findByUserIdAndStatus (Long userId, SubscriptionStatus status);
}