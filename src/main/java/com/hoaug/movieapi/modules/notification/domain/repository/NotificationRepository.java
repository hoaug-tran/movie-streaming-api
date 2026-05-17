package com.hoaug.movieapi.modules.notification.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.notification.domain.model.Notification;

public interface NotificationRepository {

  Optional<Notification> findById(Long id);

  Notification save(Notification notification);

  List<Notification> findAll();

  List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

  Long countByUserIdAndIsReadFalse(Long userId);

  void markAllAsRead(Long userId);

  void deleteById(Long id);

  List<Long> findAllActiveUserIds();

  // Returns [id, email, fullName] for each active user with email
  List<Object[]> findAllActiveUsersWithEmail();

  boolean existsByUserIdAndTypeAndCreatedAtAfter(Long userId, String type, LocalDateTime after);
}