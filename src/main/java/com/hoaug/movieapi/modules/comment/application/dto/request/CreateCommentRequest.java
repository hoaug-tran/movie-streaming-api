package com.hoaug.movieapi.modules.comment.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateCommentRequest {

  @NotNull
  private Long movieId;

  private Long parentCommentId;

  @NotBlank
  @Size(max = 5000)
  private String content;

  public Long getMovieId () {
    return movieId;
  }

  public void setMovieId (Long movieId) {
    this.movieId = movieId;
  }

  public Long getParentCommentId () {
    return parentCommentId;
  }

  public void setParentCommentId (Long parentCommentId) {
    this.parentCommentId = parentCommentId;
  }

  public String getContent () {
    return content;
  }

  public void setContent (String content) {
    this.content = content;
  }
}