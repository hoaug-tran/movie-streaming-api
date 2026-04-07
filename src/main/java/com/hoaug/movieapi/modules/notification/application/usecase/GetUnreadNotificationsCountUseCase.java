package com.hoaug.movieapi.modules.notification.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;

@Component
public class GetUnreadNotificationsCountUseCase {

  private final NotificationRepository notificationRepository;

  public GetUnreadNotificationsCountUseCase(NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
  }

  public Long execute (Long userId) {
    return notificationRepository.countByUserIdAndIsReadFalse(userId);
  }
}