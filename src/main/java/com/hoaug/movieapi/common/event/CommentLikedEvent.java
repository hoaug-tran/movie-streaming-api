package com.hoaug.movieapi.common.event;

public class CommentLikedEvent extends DomainEvent {

  private final Long commentId;
  private final Long commentOwnerId;
  private final Long likerUserId;
  private final Long movieId;
  private final String movieSlug;

  public CommentLikedEvent(Long commentId, Long commentOwnerId, Long likerUserId,
      Long movieId, String movieSlug) {
    this.commentId = commentId;
    this.commentOwnerId = commentOwnerId;
    this.likerUserId = likerUserId;
    this.movieId = movieId;
    this.movieSlug = movieSlug;
  }

  public Long getCommentId() {
    return commentId;
  }

  public Long getCommentOwnerId() {
    return commentOwnerId;
  }

  public Long getLikerUserId() {
    return likerUserId;
  }

  public Long getMovieId() {
    return movieId;
  }

  public String getMovieSlug() {
    return movieSlug;
  }
}
