package com.hoaug.movieapi.modules.comment.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;
import com.hoaug.movieapi.modules.comment.domain.model.CommentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AdminCommentRequest {

  @NotNull(message = "User ID is required")
  private Long userId;

  @NotNull(message = "Vui lòng chọn phim.")
  private Long movieId;

  private Long episodeId;

  private Long parentCommentId;

  @NotBlank(message = "Comment content is required")
  @Size(min = 1, max = 5000, message = "Comment must be between 1 and 5000 characters")
  @ValidSafeString(minLength = 1, maxLength = 5000)
  private String content;

  @NotNull(message = "Comment status is required")
  private CommentStatus status;

  public Long getUserId () {
    return userId;
  }

  public void setUserId (Long userId) {
    this.userId = userId;
  }

  public Long getMovieId () {
    return movieId;
  }

  public void setMovieId (Long movieId) {
    this.movieId = movieId;
  }

  public Long getEpisodeId () {
    return episodeId;
  }

  public void setEpisodeId (Long episodeId) {
    this.episodeId = episodeId;
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

  public CommentStatus getStatus () {
    return status;
  }

  public void setStatus (CommentStatus status) {
    this.status = status;
  }
}
