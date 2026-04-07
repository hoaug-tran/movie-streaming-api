package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity.UserSubscriptionEntity;

public interface JpaUserSubscriptionRepository extends JpaRepository<UserSubscriptionEntity, Long> {

  List<UserSubscriptionEntity> findByUserIdOrderByCreatedAtDesc (Long userId);
}