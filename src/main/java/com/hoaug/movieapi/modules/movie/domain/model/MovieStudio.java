package com.hoaug.movieapi.modules.movie.domain.model;

import java.time.LocalDateTime;

public class MovieStudio {
  private Long id;
  private Long movieId;
  private Long studioId;
  private MovieStudioRole role;
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

  public Long getStudioId () {
    return studioId;
  }

  public void setStudioId (Long studioId) {
    this.studioId = studioId;
  }

  public MovieStudioRole getRole () {
    return role;
  }

  public void setRole (MovieStudioRole role) {
    this.role = role;
  }

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
