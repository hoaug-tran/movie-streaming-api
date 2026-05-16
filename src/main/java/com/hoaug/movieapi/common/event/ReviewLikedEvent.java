package com.hoaug.movieapi.common.event;

public class ReviewLikedEvent extends DomainEvent {

  private final Long reviewId;
  private final Long reviewOwnerId;
  private final Long likerUserId;
  private final Long movieId;

  public ReviewLikedEvent(Long reviewId, Long reviewOwnerId, Long likerUserId, Long movieId) {
    this.reviewId = reviewId;
    this.reviewOwnerId = reviewOwnerId;
    this.likerUserId = likerUserId;
    this.movieId = movieId;
  }

  public Long getReviewId() {
    return reviewId;
  }

  public Long getReviewOwnerId() {
    return reviewOwnerId;
  }

  public Long getLikerUserId() {
    return likerUserId;
  }

  public Long getMovieId() {
    return movieId;
  }
}
