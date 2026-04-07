package com.hoaug.movieapi.modules.movie.application.dto.request;

public class CreateMovieStudioRequest {
  private Long studioId;
  private String role;

  public Long getStudioId () {
    return studioId;
  }

  public void setStudioId (Long studioId) {
    this.studioId = studioId;
  }

  public String getRole () {
    return role;
  }

  public void setRole (String role) {
    this.role = role;
  }
}
