package com.hoaug.movieapi.modules.searchhistory.application.dto.response;

import java.time.LocalDateTime;

public class SearchHistoryResponse {

  private Long id;
  private String keyword;
  private LocalDateTime searchedAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public String getKeyword () {
    return keyword;
  }

  public void setKeyword (String keyword) {
    this.keyword = keyword;
  }

  public LocalDateTime getSearchedAt () {
    return searchedAt;
  }

  public void setSearchedAt (LocalDateTime searchedAt) {
    this.searchedAt = searchedAt;
  }
}