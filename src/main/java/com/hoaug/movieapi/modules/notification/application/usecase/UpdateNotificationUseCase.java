package com.hoaug.movieapi.modules.notification.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.notification.application.dto.request.UpdateNotificationRequest;
import com.hoaug.movieapi.modules.notification.application.dto.response.NotificationResponse;
import com.hoaug.movieapi.modules.notification.application.mapper.NotificationMapper;
import com.hoaug.movieapi.modules.notification.domain.model.Notification;
import com.hoaug.movieapi.modules.notification.domain.model.NotificationType;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;

@Component
public class UpdateNotificationUseCase {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  public UpdateNotificationUseCase(NotificationRepository notificationRepository,
      NotificationMapper notificationMapper) {
    this.notificationRepository = notificationRepository;
    this.notificationMapper = notificationMapper;
  }

  public NotificationResponse execute(Long notificationId, UpdateNotificationRequest request) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

    notification.setTitle(request.getTitle());
    notification.setContent(request.getContent());
    notification.setType(NotificationType.valueOf(request.getType()));
    notification.setActionUrl(request.getActionUrl());

    Notification saved = notificationRepository.save(notification);
    return notificationMapper.toResponse(saved);
  }
}
