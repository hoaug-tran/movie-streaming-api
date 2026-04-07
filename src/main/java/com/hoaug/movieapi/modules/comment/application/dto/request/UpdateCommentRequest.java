package com.hoaug.movieapi.modules.comment.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateCommentRequest {

  @NotBlank
  @Size(max = 5000)
  private String content;

  public String getContent () {
    return content;
  }

  public void setContent (String content) {
    this.content = content;
  }
}