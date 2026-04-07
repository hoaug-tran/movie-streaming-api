package com.hoaug.movieapi.modules.notification.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.notification.domain.model.Notification;

public interface NotificationRepository {

  Optional<Notification> findById (Long id);

  Notification save (Notification notification);

  List<Notification> findByUserIdOrderByCreatedAtDesc (Long userId);

  Long countByUserIdAndIsReadFalse (Long userId);
}