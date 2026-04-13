package com.hoaug.movieapi.modules.notification.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MarkNotificationAsReadRequest {

  @NotNull(message = "Notification ID is required")
  @Positive(message = "Notification ID must be positive")
  private Long notificationId;

  public Long getNotificationId () {
    return notificationId;
  }

  public void setNotificationId (Long notificationId) {
    this.notificationId = notificationId;
  }
}
