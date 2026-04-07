package com.hoaug.movieapi.modules.movie.application.dto.response;

import java.util.List;

public class SearchMovieResponse {
  private List<MovieBasicResponse> content;
  private int totalPages;
  private long totalElements;
  private int currentPage;
  private int pageSize;

  public SearchMovieResponse(List<MovieBasicResponse> content, int totalPages, long totalElements,
      int currentPage, int pageSize) {
    this.content = content;
    this.totalPages = totalPages;
    this.totalElements = totalElements;
    this.currentPage = currentPage;
    this.pageSize = pageSize;
  }

  public List<MovieBasicResponse> getContent () {
    return content;
  }

  public void setContent (List<MovieBasicResponse> content) {
    this.content = content;
  }

  public int getTotalPages () {
    return totalPages;
  }

  public void setTotalPages (int totalPages) {
    this.totalPages = totalPages;
  }

  public long getTotalElements () {
    return totalElements;
  }

  public void setTotalElements (long totalElements) {
    this.totalElements = totalElements;
  }

  public int getCurrentPage () {
    return currentPage;
  }

  public void setCurrentPage (int currentPage) {
    this.currentPage = currentPage;
  }

  public int getPageSize () {
    return pageSize;
  }

  public void setPageSize (int pageSize) {
    this.pageSize = pageSize;
  }
}
