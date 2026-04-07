package com.hoaug.movieapi.modules.review.application.dto.response;

import java.time.LocalDateTime;

import com.hoaug.movieapi.modules.review.domain.model.ReviewStatus;

public class ReviewResponse {
  private Long id;
  private Long movieId;
  private Integer rating;
  private String title;
  private String content;
  private Boolean isEdited;
  private ReviewStatus status;
  private Integer likeCount;
  private LocalDateTime createdAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public Long getMovieId () {
    return movieId;
  }

  public void setMovieId (Long movieId) {
    this.movieId = movieId;
  }

  public Integer getRating () {
    return rating;
  }

  public void setRating (Integer rating) {
    this.rating = rating;
  }

  public String getTitle () {
    return title;
  }

  public void setTitle (String title) {
    this.title = title;
  }

  public String getContent () {
    return content;
  }

  public void setContent (String content) {
    this.content = content;
  }

  public Boolean getIsEdited () {
    return isEdited;
  }

  public void setIsEdited (Boolean isEdited) {
    this.isEdited = isEdited;
  }

  public ReviewStatus getStatus () {
    return status;
  }

  public void setStatus (ReviewStatus status) {
    this.status = status;
  }

  public Integer getLikeCount () {
    return likeCount;
  }

  public void setLikeCount (Integer likeCount) {
    this.likeCount = likeCount;
  }

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}