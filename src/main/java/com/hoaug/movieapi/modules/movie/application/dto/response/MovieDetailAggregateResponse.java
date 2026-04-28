package com.hoaug.movieapi.modules.movie.application.dto.response;

import java.util.List;

import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;

public class MovieDetailAggregateResponse {
  private MovieDetailResponse movie;
  private List<CommentResponse> comments;
  private List<ReviewResponse> reviews;

  public MovieDetailResponse getMovie () {
    return movie;
  }

  public void setMovie (MovieDetailResponse movie) {
    this.movie = movie;
  }

  public List<CommentResponse> getComments () {
    return comments;
  }

  public void setComments (List<CommentResponse> comments) {
    this.comments = comments;
  }

  public List<ReviewResponse> getReviews () {
    return reviews;
  }

  public void setReviews (List<ReviewResponse> reviews) {
    this.reviews = reviews;
  }
}
