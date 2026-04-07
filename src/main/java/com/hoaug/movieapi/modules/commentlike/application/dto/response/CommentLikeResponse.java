package com.hoaug.movieapi.modules.commentlike.application.dto.response;

public class CommentLikeResponse {

  private Long commentId;
  private Boolean liked;

  public Long getCommentId () {
    return commentId;
  }

  public void setCommentId (Long commentId) {
    this.commentId = commentId;
  }

  public Boolean getLiked () {
    return liked;
  }

  public void setLiked (Boolean liked) {
    this.liked = liked;
  }
}