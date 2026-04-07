package com.hoaug.movieapi.modules.notification.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.hoaug.movieapi.modules.notification.domain.model.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class NotificationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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

  @Column(name = "created_at", nullable = false)
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