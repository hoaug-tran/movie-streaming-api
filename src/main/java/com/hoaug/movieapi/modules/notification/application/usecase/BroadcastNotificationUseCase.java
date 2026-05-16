package com.hoaug.movieapi.modules.notification.application.usecase;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.notification.application.dto.request.BroadcastNotificationRequest;
import com.hoaug.movieapi.modules.notification.domain.model.Notification;
import com.hoaug.movieapi.modules.notification.domain.model.NotificationType;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;

@Component
public class BroadcastNotificationUseCase {

  private final NotificationRepository notificationRepository;

  public BroadcastNotificationUseCase(NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
  }

  public int execute(BroadcastNotificationRequest request) {
    List<Long> userIds = notificationRepository.findAllActiveUserIds();
    NotificationType type = NotificationType.valueOf(request.getType());
    LocalDateTime now = LocalDateTime.now();

    for (Long userId : userIds) {
      Notification notification = new Notification();
      notification.setUserId(userId);
      notification.setTitle(request.getTitle());
      notification.setContent(request.getContent());
      notification.setType(type);
      notification.setIsRead(false);
      notification.setActionUrl(request.getActionUrl());
      notification.setCreatedAt(now);
      notificationRepository.save(notification);
    }

    return userIds.size();
  }
}
