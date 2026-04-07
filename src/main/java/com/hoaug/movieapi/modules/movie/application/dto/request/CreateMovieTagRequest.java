package com.hoaug.movieapi.modules.movie.application.dto.request;

public class CreateMovieTagRequest {
  private Long tagId;

  public Long getTagId () {
    return tagId;
  }

  public void setTagId (Long tagId) {
    this.tagId = tagId;
  }
}
