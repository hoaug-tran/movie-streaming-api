package com.hoaug.movieapi.modules.movie.application.dto.request;

import jakarta.validation.constraints.NotNull;

public class UpdateMovieInteractionLocksRequest {
  @NotNull
  private Boolean commentsLocked;

  @NotNull
  private Boolean reviewsLocked;

  public Boolean getCommentsLocked () {
    return commentsLocked;
  }

  public void setCommentsLocked (Boolean commentsLocked) {
    this.commentsLocked = commentsLocked;
  }

  public Boolean getReviewsLocked () {
    return reviewsLocked;
  }

  public void setReviewsLocked (Boolean reviewsLocked) {
    this.reviewsLocked = reviewsLocked;
  }
}
