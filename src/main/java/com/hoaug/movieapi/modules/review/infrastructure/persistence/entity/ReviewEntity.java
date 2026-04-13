package com.hoaug.movieapi.modules.review.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.hoaug.movieapi.common.model.BaseEntity;
import com.hoaug.movieapi.modules.review.domain.model.ReviewStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id",
    "movie_id" }))
public class ReviewEntity extends BaseEntity {

  private Long userId;
  private Long movieId;
  private Integer rating;
  private String title;
  private String content;
  @Column(name = "is_edited")
  private Boolean isEdited;
  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ReviewStatus status;
  @Column(name = "like_count")
  private Integer likeCount;

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

  public Integer getRating () {
    return rating;
  }

  public void setRating (Integer rating) {
    this.rating = rating;
  }

  public String getContent () {
    return content;
  }

  public void setContent (String content) {
    this.content = content;
  }

  public String getTitle () {
    return title;
  }

  public void setTitle (String title) {
    this.title = title;
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

  public LocalDateTime getUpdatedAt () {
    return updatedAt;
  }

  public void setUpdatedAt (LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}