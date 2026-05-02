package com.hoaug.movieapi.modules.subscription.infrastructure.persistence.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.UserSubscription;
import com.hoaug.movieapi.modules.subscription.domain.repository.UserSubscriptionRepository;
import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.entity.UserSubscriptionEntity;
import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.repository.JpaUserSubscriptionRepository;

@Component
public class UserSubscriptionRepositoryAdapter implements UserSubscriptionRepository {

  private final JpaUserSubscriptionRepository jpaUserSubscriptionRepository;

  public UserSubscriptionRepositoryAdapter(
      JpaUserSubscriptionRepository jpaUserSubscriptionRepository) {
    this.jpaUserSubscriptionRepository = jpaUserSubscriptionRepository;
  }

  @Override
  public Optional<UserSubscription> findById (Long id) {
    return jpaUserSubscriptionRepository.findById(id).map(this::toDomain);
  }

  @Override
  public UserSubscription save (UserSubscription userSubscription) {
    return toDomain(jpaUserSubscriptionRepository.save(toEntity(userSubscription)));
  }

  @Override
  public List<UserSubscription> findByUserIdOrderByCreatedAtDesc (Long userId) {
    return jpaUserSubscriptionRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
        .map(this::toDomain).toList();
  }

  @Override
  public List<UserSubscription> findByStatusAndEndAtBefore (SubscriptionStatus status,
      LocalDateTime dateTime) {
    return jpaUserSubscriptionRepository.findByStatusAndEndAtBefore(status, dateTime).stream()
        .map(this::toDomain).toList();
  }

  @Override
  public List<UserSubscription> findByStatusAndEndAtBetween (SubscriptionStatus status,
      LocalDateTime startDate, LocalDateTime endDate) {
    return jpaUserSubscriptionRepository.findByStatusAndEndAtBetween(status, startDate, endDate)
        .stream().map(this::toDomain).toList();
  }

  @Override
  public Optional<UserSubscription> findFirstByUserIdAndStatusOrderByEndAtDesc (Long userId,
      SubscriptionStatus status) {
    return jpaUserSubscriptionRepository
        .findFirstByUserIdAndStatusOrderByEndAtDesc(userId, status).map(this::toDomain);
  }

  @Override
  public Optional<UserSubscription> findActiveByUserId (Long userId) {
    return jpaUserSubscriptionRepository.findActiveByUserId(userId).map(this::toDomain);
  }

  @Override
  public List<UserSubscription> findByStatusAndCreatedAtBefore (SubscriptionStatus status,
      LocalDateTime dateTime) {
    return jpaUserSubscriptionRepository.findByStatusAndCreatedAtBefore(status, dateTime).stream()
        .map(this::toDomain).toList();
  }

  private UserSubscription toDomain (UserSubscriptionEntity entity) {
    UserSubscription subscription = new UserSubscription();
    subscription.setId(entity.getId());
    subscription.setUserId(entity.getUserId());
    subscription.setPlanId(entity.getPlanId());
    subscription.setStartAt(entity.getStartAt());
    subscription.setEndAt(entity.getEndAt());
    subscription.setStatus(entity.getStatus());
    subscription.setAutoRenew(entity.getAutoRenew());
    subscription.setCreatedAt(entity.getCreatedAt());
    subscription.setUpdatedAt(entity.getUpdatedAt());
    return subscription;
  }

  private UserSubscriptionEntity toEntity (UserSubscription subscription) {
    UserSubscriptionEntity entity = new UserSubscriptionEntity();
    entity.setId(subscription.getId());
    entity.setUserId(subscription.getUserId());
    entity.setPlanId(subscription.getPlanId());
    entity.setStartAt(subscription.getStartAt());
    entity.setEndAt(subscription.getEndAt());
    entity.setStatus(subscription.getStatus());
    entity.setAutoRenew(subscription.getAutoRenew());
    entity.setCreatedAt(subscription.getCreatedAt());
    entity.setUpdatedAt(subscription.getUpdatedAt());
    return entity;
  }
}