package com.hoaug.movieapi.modules.notification.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.notification.infrastructure.persistence.entity.PushSubscriptionEntity;

public interface JpaPushSubscriptionRepository extends JpaRepository<PushSubscriptionEntity, Long> {
  Optional<PushSubscriptionEntity> findByUserIdAndEndpoint (Long userId, String endpoint);
  List<PushSubscriptionEntity> findByUserId (Long userId);
  void deleteByUserIdAndEndpoint (Long userId, String endpoint);
}
