package com.hoaug.movieapi.modules.notification.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.notification.application.dto.response.NotificationResponse;
import com.hoaug.movieapi.modules.notification.application.mapper.NotificationMapper;
import com.hoaug.movieapi.modules.notification.domain.model.Notification;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;

@Component
public class MarkNotificationReadUseCase {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  public MarkNotificationReadUseCase(NotificationRepository notificationRepository,
      NotificationMapper notificationMapper) {
    this.notificationRepository = notificationRepository;
    this.notificationMapper = notificationMapper;
  }

  public NotificationResponse execute (Long userId, Long notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow( () -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

    if (!notification.getUserId().equals(userId)) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    notification.setIsRead(true);

    Notification savedNotification = notificationRepository.save(notification);
    return notificationMapper.toResponse(savedNotification);
  }
}