package com.hoaug.movieapi.modules.notification.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.notification.domain.model.Notification;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;

@Component
public class DeleteNotificationUseCase {

  private final NotificationRepository notificationRepository;

  public DeleteNotificationUseCase(NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
  }

  public void execute (Long userId, Long notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow( () -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

    if (!notification.getUserId().equals(userId)) {
      throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    notificationRepository.deleteById(notificationId);
  }
}
