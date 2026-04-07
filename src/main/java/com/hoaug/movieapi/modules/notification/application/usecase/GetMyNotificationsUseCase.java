package com.hoaug.movieapi.modules.notification.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.notification.application.dto.response.NotificationResponse;
import com.hoaug.movieapi.modules.notification.application.mapper.NotificationMapper;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;

@Component
public class GetMyNotificationsUseCase {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  public GetMyNotificationsUseCase(NotificationRepository notificationRepository,
      NotificationMapper notificationMapper) {
    this.notificationRepository = notificationRepository;
    this.notificationMapper = notificationMapper;
  }

  public List<NotificationResponse> execute (Long userId) {
    return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
        .map(notificationMapper::toResponse).toList();
  }
}