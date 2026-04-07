package com.hoaug.movieapi.modules.movie.application.dto.request;

public class SearchMovieRequest {
  private String keyword;
  private Long categoryId;
  private Long tagId;
  private Integer fromYear;
  private Integer toYear;
  private Double minRating;
  private String sortBy;
  private String sortDirection;
  private Integer page;
  private Integer size;

  public String getKeyword () {
    return keyword;
  }

  public void setKeyword (String keyword) {
    this.keyword = keyword;
  }

  public Long getCategoryId () {
    return categoryId;
  }

  public void setCategoryId (Long categoryId) {
    this.categoryId = categoryId;
  }

  public Long getTagId () {
    return tagId;
  }

  public void setTagId (Long tagId) {
    this.tagId = tagId;
  }

  public Integer getFromYear () {
    return fromYear;
  }

  public void setFromYear (Integer fromYear) {
    this.fromYear = fromYear;
  }

  public Integer getToYear () {
    return toYear;
  }

  public void setToYear (Integer toYear) {
    this.toYear = toYear;
  }

  public Double getMinRating () {
    return minRating;
  }

  public void setMinRating (Double minRating) {
    this.minRating = minRating;
  }

  public String getSortBy () {
    return sortBy;
  }

  public void setSortBy (String sortBy) {
    this.sortBy = sortBy;
  }

  public String getSortDirection () {
    return sortDirection;
  }

  public void setSortDirection (String sortDirection) {
    this.sortDirection = sortDirection;
  }

  public Integer getPage () {
    return page;
  }

  public void setPage (Integer page) {
    this.page = page;
  }

  public Integer getSize () {
    return size;
  }

  public void setSize (Integer size) {
    this.size = size;
  }
}
