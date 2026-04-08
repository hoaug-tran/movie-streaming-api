package com.hoaug.movieapi.modules.notification.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;

@Component
public class MarkAllNotificationsAsReadUseCase {

  private final NotificationRepository notificationRepository;

  public MarkAllNotificationsAsReadUseCase(NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
  }

  public void execute (Long userId) {
    notificationRepository.markAllAsRead(userId);
  }
}
