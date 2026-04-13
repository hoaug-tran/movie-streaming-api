package com.hoaug.movieapi.modules.searchhistory.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.hoaug.movieapi.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "search_histories")
public class SearchHistoryEntity extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false, length = 255)
  private String keyword;

  @Column(name = "searched_at", nullable = false)
  private LocalDateTime searchedAt;

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