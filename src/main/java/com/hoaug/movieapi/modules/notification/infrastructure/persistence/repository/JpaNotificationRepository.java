package com.hoaug.movieapi.modules.notification.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hoaug.movieapi.modules.notification.domain.model.NotificationType;
import com.hoaug.movieapi.modules.notification.infrastructure.persistence.entity.NotificationEntity;

public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Long> {

  List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

  List<NotificationEntity> findAllByOrderByCreatedAtDesc();

  Long countByUserIdAndIsReadFalse(Long userId);

  boolean existsByUserIdAndTypeAndCreatedAtAfter(Long userId, NotificationType type,
      LocalDateTime after);

  @Query("SELECT DISTINCT u.id FROM UserEntity u WHERE u.accountStatus = 'ACTIVE'")
  List<Long> findAllActiveUserIds();

  @Query("SELECT u.id, u.email, u.fullName FROM UserEntity u WHERE u.accountStatus = 'ACTIVE' AND u.email IS NOT NULL")
  List<Object[]> findAllActiveUsersWithEmail();
}