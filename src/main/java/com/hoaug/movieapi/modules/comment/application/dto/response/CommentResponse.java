package com.hoaug.movieapi.modules.comment.application.dto.response;

import java.time.LocalDateTime;

public class CommentResponse {

  private Long id;
  private Long userId;
  private Long movieId;
  private String movieSlug;
  private String movieTitle;
  private Long episodeId;
  private Long parentCommentId;
  private String content;
  private Integer likeCount;
  private Integer replyCount;
  private String status;
  private String authorUsername;
  private String authorFullName;
  private String authorAvatarUrl;
  private Boolean likedByCurrentUser;
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

  public String getMovieSlug () {
    return movieSlug;
  }

  public void setMovieSlug (String movieSlug) {
    this.movieSlug = movieSlug;
  }

  public String getMovieTitle () {
    return movieTitle;
  }

  public void setMovieTitle (String movieTitle) {
    this.movieTitle = movieTitle;
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

  public String getStatus () {
    return status;
  }

  public void setStatus (String status) {
    this.status = status;
  }

  public String getAuthorUsername () {
    return authorUsername;
  }

  public void setAuthorUsername (String authorUsername) {
    this.authorUsername = authorUsername;
  }

  public String getAuthorFullName () {
    return authorFullName;
  }

  public void setAuthorFullName (String authorFullName) {
    this.authorFullName = authorFullName;
  }

  public String getAuthorAvatarUrl () {
    return authorAvatarUrl;
  }

  public void setAuthorAvatarUrl (String authorAvatarUrl) {
    this.authorAvatarUrl = authorAvatarUrl;
  }

  public Boolean getLikedByCurrentUser () {
    return likedByCurrentUser;
  }

  public void setLikedByCurrentUser (Boolean likedByCurrentUser) {
    this.likedByCurrentUser = likedByCurrentUser;
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