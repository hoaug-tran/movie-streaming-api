package com.hoaug.movieapi.common.event;

public class ReviewCreatedEvent extends DomainEvent {
  private final Long reviewId;
  private final Long movieId;
  private final Long userId;
  private final Integer rating;

  public ReviewCreatedEvent(Long reviewId, Long movieId, Long userId, Integer rating) {
    this.reviewId = reviewId;
    this.movieId = movieId;
    this.userId = userId;
    this.rating = rating;
  }

  public Long getReviewId () {
    return reviewId;
  }

  public Long getMovieId () {
    return movieId;
  }

  public Long getUserId () {
    return userId;
  }

  public Integer getRating () {
    return rating;
  }
}
