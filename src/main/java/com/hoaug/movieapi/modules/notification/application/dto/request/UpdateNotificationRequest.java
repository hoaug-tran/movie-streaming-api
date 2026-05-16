package com.hoaug.movieapi.modules.notification.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateNotificationRequest {

  @NotBlank(message = "Tiêu đề thông báo là bắt buộc")
  @Size(min = 1, max = 255, message = "Tiêu đề phải từ 1 đến 255 ký tự")
  @ValidSafeString(minLength = 1, maxLength = 255)
  private String title;

  @NotBlank(message = "Nội dung thông báo là bắt buộc")
  @Size(min = 1, max = 5000, message = "Nội dung phải từ 1 đến 5000 ký tự")
  @ValidSafeString(minLength = 1, maxLength = 5000)
  private String content;

  @NotBlank(message = "Loại thông báo là bắt buộc")
  @Size(min = 1, max = 50, message = "Loại thông báo phải từ 1 đến 50 ký tự")
  @ValidSafeString(minLength = 1, maxLength = 50)
  private String type;

  @Size(max = 500, message = "URL hành động không được vượt quá 500 ký tự")
  private String actionUrl;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getActionUrl() {
    return actionUrl;
  }

  public void setActionUrl(String actionUrl) {
    this.actionUrl = actionUrl;
  }
}
