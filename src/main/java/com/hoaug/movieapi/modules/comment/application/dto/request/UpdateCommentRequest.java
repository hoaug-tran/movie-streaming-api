package com.hoaug.movieapi.modules.comment.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateCommentRequest {

  @NotBlank(message = "Comment content is required")
  @Size(min = 1, max = 5000, message = "Comment must be between 1 and 5000 characters")
  @ValidSafeString(minLength = 1, maxLength = 5000)
  private String content;

  public String getContent () {
    return content;
  }

  public void setContent (String content) {
    this.content = content;
  }
}