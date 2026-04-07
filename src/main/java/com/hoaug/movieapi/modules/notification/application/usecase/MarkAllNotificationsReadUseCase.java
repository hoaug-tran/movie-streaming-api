package com.hoaug.movieapi.modules.notification.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.notification.domain.model.Notification;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;

@Component
public class MarkAllNotificationsReadUseCase {

  private final NotificationRepository notificationRepository;

  public MarkAllNotificationsReadUseCase(NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
  }

  public void execute (Long userId) {
    List<Notification> notifications = notificationRepository
        .findByUserIdOrderByCreatedAtDesc(userId);

    for (Notification notification : notifications) {
      if (!Boolean.TRUE.equals(notification.getIsRead())) {
        notification.setIsRead(true);
        notificationRepository.save(notification);
      }
    }
  }
}