package com.hoaug.movieapi.modules.notification.application.dto.response;

import java.time.LocalDateTime;

public class NotificationResponse {

  private Long id;
  private String title;
  private String content;
  private String type;
  private Boolean isRead;
  private LocalDateTime createdAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
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

  public String getType () {
    return type;
  }

  public void setType (String type) {
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