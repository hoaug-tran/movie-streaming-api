package com.hoaug.movieapi.common.event;

public class CommentCreatedEvent extends DomainEvent {

  private final Long commentId;
  private final Long parentCommentId;
  private final Long parentCommentUserId;
  private final Long movieId;
  private final String movieSlug;
  private final Long episodeId;
  private final Long authorUserId;

  public CommentCreatedEvent(Long commentId, Long parentCommentId, Long parentCommentUserId,
      Long movieId, String movieSlug, Long episodeId, Long authorUserId) {
    this.commentId = commentId;
    this.parentCommentId = parentCommentId;
    this.parentCommentUserId = parentCommentUserId;
    this.movieId = movieId;
    this.movieSlug = movieSlug;
    this.episodeId = episodeId;
    this.authorUserId = authorUserId;
  }

  public Long getCommentId() {
    return commentId;
  }

  public Long getParentCommentId() {
    return parentCommentId;
  }

  public Long getParentCommentUserId() {
    return parentCommentUserId;
  }

  public Long getMovieId() {
    return movieId;
  }

  public String getMovieSlug() {
    return movieSlug;
  }

  public Long getEpisodeId() {
    return episodeId;
  }

  public Long getAuthorUserId() {
    return authorUserId;
  }
}
