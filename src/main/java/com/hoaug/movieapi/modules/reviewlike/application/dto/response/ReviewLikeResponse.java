package com.hoaug.movieapi.modules.reviewlike.application.dto.response;

public class ReviewLikeResponse {

  private Long reviewId;
  private Boolean liked;

  public Long getReviewId () {
    return reviewId;
  }

  public void setReviewId (Long reviewId) {
    this.reviewId = reviewId;
  }

  public Boolean getLiked () {
    return liked;
  }

  public void setLiked (Boolean liked) {
    this.liked = liked;
  }
}