package com.hoaug.movieapi.modules.movie.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCategoryRequest {

  @NotBlank(message = "Category name is required")
  @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
  @ValidSafeString(minLength = 1, maxLength = 100)
  private String name;

  @NotBlank(message = "Category slug is required")
  @Size(min = 1, max = 120, message = "Slug must be between 1 and 120 characters")
  @ValidSafeString(minLength = 1, maxLength = 120)
  private String slug;

  @Size(max = 500, message = "Description must be at most 500 characters")
  @ValidSafeString(minLength = 0, maxLength = 500)
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
