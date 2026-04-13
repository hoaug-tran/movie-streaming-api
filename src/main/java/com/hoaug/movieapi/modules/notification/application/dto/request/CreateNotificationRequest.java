package com.hoaug.movieapi.modules.notification.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateNotificationRequest {

  @NotNull(message = "User ID is required")
  @Positive(message = "User ID must be positive")
  private Long userId;

  @NotBlank(message = "Notification title is required")
  @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
  @ValidSafeString(minLength = 1, maxLength = 255)
  private String title;

  @NotBlank(message = "Notification content is required")
  @Size(min = 1, max = 5000, message = "Content must be between 1 and 5000 characters")
  @ValidSafeString(minLength = 1, maxLength = 5000)
  private String content;

  @NotBlank(message = "Notification type is required")
  @Size(min = 1, max = 50, message = "Type must be between 1 and 50 characters")
  @ValidSafeString(minLength = 1, maxLength = 50)
  private String type;

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

  public String getType () {
    return type;
  }

  public void setType (String type) {
    this.type = type;
  }
}