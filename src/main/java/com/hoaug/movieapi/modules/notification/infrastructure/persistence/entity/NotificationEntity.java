package com.hoaug.movieapi.modules.notification.infrastructure.persistence.entity;

import com.hoaug.movieapi.common.model.BaseEntity;
import com.hoaug.movieapi.modules.notification.domain.model.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class NotificationEntity extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false, length = 255)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private NotificationType type;

  @Column(name = "is_read", nullable = false)
  private Boolean isRead;

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
}