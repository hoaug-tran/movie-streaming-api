package com.hoaug.movieapi.modules.notification.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.notification.application.dto.request.CreateNotificationRequest;
import com.hoaug.movieapi.modules.notification.application.dto.response.NotificationResponse;
import com.hoaug.movieapi.modules.notification.application.mapper.NotificationMapper;
import com.hoaug.movieapi.modules.notification.domain.model.Notification;
import com.hoaug.movieapi.modules.notification.domain.model.NotificationType;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;

@Component
public class CreateNotificationUseCase {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  public CreateNotificationUseCase(NotificationRepository notificationRepository,
      NotificationMapper notificationMapper) {
    this.notificationRepository = notificationRepository;
    this.notificationMapper = notificationMapper;
  }

  public NotificationResponse execute (CreateNotificationRequest request) {
    Notification notification = new Notification();
    notification.setUserId(request.getUserId());
    notification.setTitle(request.getTitle());
    notification.setContent(request.getContent());
    notification.setType(NotificationType.valueOf(request.getType()));
    notification.setIsRead(false);
    notification.setCreatedAt(LocalDateTime.now());

    Notification savedNotification = notificationRepository.save(notification);
    return notificationMapper.toResponse(savedNotification);
  }
}