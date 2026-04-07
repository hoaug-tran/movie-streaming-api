package com.hoaug.movieapi.modules.searchhistory.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "search_histories")
public class SearchHistoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false, length = 255)
  private String keyword;

  @Column(name = "searched_at", nullable = false)
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