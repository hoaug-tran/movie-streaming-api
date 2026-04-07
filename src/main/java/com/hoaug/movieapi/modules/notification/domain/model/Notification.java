package com.hoaug.movieapi.modules.notification.domain.model;

import java.time.LocalDateTime;

public class Notification {

  private Long id;
  private Long userId;
  private String title;
  private String content;
  private NotificationType type;
  private Boolean isRead;
  private LocalDateTime createdAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public Long getUserId () {
    return userId;
  }

  public void setUserId (Long userId) {
    this.userId = userId;
  }

  public String getTitle () {
    return title;
  }

  public void setTitle (String title) {
    this.title = title;
  }

  public String getContent () {
    return content;
  }

  public void setContent (String content) {
    this.content = content;
  }

  public NotificationType getType () {
    return type;
  }

  public void setType (NotificationType type) {
    this.type = type;
  }

  public Boolean getIsRead () {
    return isRead;
  }

  public void setIsRead (Boolean read) {
    isRead = read;
  }

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}