package com.hoaug.movieapi.modules.movie.application.dto.response;

public class MovieStudioResponse {
  private Long id;
  private StudioResponse studio;
  private String role;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public StudioResponse getStudio () {
    return studio;
  }

  public void setStudio (StudioResponse studio) {
    this.studio = studio;
  }

  public String getRole () {
    return role;
  }

  public void setRole (String role) {
    this.role = role;
  }
}
