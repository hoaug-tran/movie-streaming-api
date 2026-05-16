package com.hoaug.movieapi.modules.notification.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.notification.application.dto.response.NotificationResponse;
import com.hoaug.movieapi.modules.notification.application.mapper.NotificationMapper;
import com.hoaug.movieapi.modules.notification.domain.repository.NotificationRepository;

@Component
public class GetAllNotificationsUseCase {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  public GetAllNotificationsUseCase(NotificationRepository notificationRepository,
      NotificationMapper notificationMapper) {
    this.notificationRepository = notificationRepository;
    this.notificationMapper = notificationMapper;
  }

  public List<NotificationResponse> execute() {
    return notificationRepository.findAll().stream()
        .map(notificationMapper::toResponse)
        .toList();
  }
}
