package com.hoaug.movieapi.modules.searchhistory.domain.model;

import java.time.LocalDateTime;

public class SearchHistory {

  private Long id;
  private Long userId;
  private String keyword;
  private LocalDateTime searchedAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public Long getUserId () {
    return userId;
  }

  public void setUserId (Long userId) {
    this.userId = userId;
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