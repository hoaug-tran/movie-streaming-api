package com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity;

import com.hoaug.movieapi.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tags")
public class TagEntity extends BaseEntity {

  @Column(nullable = false, unique = true, length = 100)
  private String name;

  @Column(nullable = false, unique = true, length = 120)
  private String slug;

  @Column(columnDefinition = "TEXT")
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