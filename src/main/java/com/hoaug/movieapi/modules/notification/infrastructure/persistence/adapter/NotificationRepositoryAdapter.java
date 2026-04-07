package com.hoaug.movieapi.modules.notification.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.notification.domain.model.Notification;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;
import com.hoaug.movieapi.modules.notification.infrastructure.persistence.entity.NotificationEntity;
import com.hoaug.movieapi.modules.notification.infrastructure.persistence.repository.JpaNotificationRepository;

@Component
public class NotificationRepositoryAdapter implements NotificationRepository {

  private final JpaNotificationRepository jpaNotificationRepository;

  public NotificationRepositoryAdapter(JpaNotificationRepository jpaNotificationRepository) {
    this.jpaNotificationRepository = jpaNotificationRepository;
  }

  @Override
  public Optional<Notification> findById (Long id) {
    return jpaNotificationRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Notification save (Notification notification) {
    NotificationEntity savedEntity = jpaNotificationRepository.save(toEntity(notification));
    return toDomain(savedEntity);
  }

  @Override
  public List<Notification> findByUserIdOrderByCreatedAtDesc (Long userId) {
    return jpaNotificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
        .map(this::toDomain).toList();
  }

  @Override
  public Long countByUserIdAndIsReadFalse (Long userId) {
    return jpaNotificationRepository.countByUserIdAndIsReadFalse(userId);
  }

  private Notification toDomain (NotificationEntity entity) {
    Notification notification = new Notification();
    notification.setId(entity.getId());
    notification.setUserId(entity.getUserId());
    notification.setTitle(entity.getTitle());
    notification.setContent(entity.getContent());
    notification.setType(entity.getType());
    notification.setIsRead(entity.getIsRead());
    notification.setCreatedAt(entity.getCreatedAt());
    return notification;
  }

  private NotificationEntity toEntity (Notification notification) {
    NotificationEntity entity = new NotificationEntity();
    entity.setId(notification.getId());
    entity.setUserId(notification.getUserId());
    entity.setTitle(notification.getTitle());
    entity.setContent(notification.getContent());
    entity.setType(notification.getType());
    entity.setIsRead(notification.getIsRead());
    entity.setCreatedAt(notification.getCreatedAt());
    return entity;
  }
}