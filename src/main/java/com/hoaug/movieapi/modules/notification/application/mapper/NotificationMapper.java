package com.hoaug.movieapi.modules.notification.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.notification.application.dto.response.NotificationResponse;
import com.hoaug.movieapi.modules.notification.domain.model.Notification;

@Component
public class NotificationMapper {

  public NotificationResponse toResponse(Notification notification) {
    NotificationResponse response = new NotificationResponse();
    response.setId(notification.getId());
    response.setTitle(notification.getTitle());
    response.setContent(notification.getContent());
    response.setType(notification.getType().name());
    response.setIsRead(notification.getIsRead());
    response.setActionUrl(notification.getActionUrl());
    response.setReferenceId(notification.getReferenceId());
    response.setCreatedAt(notification.getCreatedAt());
    return response;
  }
}