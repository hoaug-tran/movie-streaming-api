package com.hoaug.movieapi.modules.comment.domain.model;

import java.time.LocalDateTime;

public class Comment {

  private Long id;
  private Long userId;
  private Long movieId;
  private Long parentCommentId;
  private String content;
  private Integer likeCount;
  private Integer replyCount;
  private CommentStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

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

  public Integer getLikeCount () {
    return likeCount;
  }

  public void setLikeCount (Integer likeCount) {
    this.likeCount = likeCount;
  }

  public Integer getReplyCount () {
    return replyCount;
  }

  public void setReplyCount (Integer replyCount) {
    this.replyCount = replyCount;
  }

  public CommentStatus getStatus () {
    return status;
  }

  public void setStatus (CommentStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt () {
    return updatedAt;
  }

  public void setUpdatedAt (LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}