package com.hoaug.movieapi.modules.notification.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;

@Component
public class AdminDeleteNotificationUseCase {

  private final NotificationRepository notificationRepository;

  public AdminDeleteNotificationUseCase(NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
  }

  public void execute(Long notificationId) {
    notificationRepository.findById(notificationId)
        .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
    notificationRepository.deleteById(notificationId);
  }
}
