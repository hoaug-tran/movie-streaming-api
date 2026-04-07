package com.hoaug.movieapi.modules.notification.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.notification.infrastructure.persistence.entity.NotificationEntity;

public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Long> {

  List<NotificationEntity> findByUserIdOrderByCreatedAtDesc (Long userId);

  Long countByUserIdAndIsReadFalse (Long userId);
}