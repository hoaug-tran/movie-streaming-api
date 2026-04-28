package com.hoaug.movieapi.modules.comment.infrastructure.persistence.entity;

import com.hoaug.movieapi.common.model.BaseEntity;
import com.hoaug.movieapi.modules.comment.domain.model.CommentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "movie_id", nullable = false)
  private Long movieId;

  @Column(name = "episode_id")
  private Long episodeId;

  @Column(name = "parent_comment_id")
  private Long parentCommentId;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "like_count", nullable = false)
  private Integer likeCount;

  @Column(name = "reply_count", nullable = false)
  private Integer replyCount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
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
}