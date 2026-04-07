package com.hoaug.movieapi.modules.movie.application.dto.request;

public class CreateMovieCategoryRequest {
  private Long categoryId;

  public Long getCategoryId () {
    return categoryId;
  }

  public void setCategoryId (Long categoryId) {
    this.categoryId = categoryId;
  }
}
