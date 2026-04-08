package com.hoaug.movieapi.modules.notification.application.dto.request;

public class MarkNotificationAsReadRequest {

  private Long notificationId;

  public Long getNotificationId () {
    return notificationId;
  }

  public void setNotificationId (Long notificationId) {
    this.notificationId = notificationId;
  }
}
