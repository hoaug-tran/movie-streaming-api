package com.hoaug.movieapi.modules.movie.domain.model;

import java.time.LocalDateTime;

public class Category {
  private Long id;
  private String name;
  private String slug;
  private String description;
  private LocalDateTime createdAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

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
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

}
