package com.hoaug.movieapi.modules.movie.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateTagRequest {

  @NotBlank
  private String name;

  @NotBlank
  private String slug;

  private String description;

  public String getName () {
    return name;
  }

  public void setName (String name) {
    this.name = name;
  }

  public String getSlug () {
    return slug;
  }

  public void setSlug (String slug) {
    this.slug = slug;
  }

  public String getDescription () {
    return description;
  }

  public void setDescription (String description) {
    this.description = description;
  }
}
