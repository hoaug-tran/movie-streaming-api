package com.hoaug.movieapi.modules.advertisement.application.dto.response;

import java.time.LocalDateTime;

public class AdvertisementViewResponse {

  private Long id;
  private Long advertisementId;
  private Long userId;
  private Long movieId;
  private Long episodeId;
  private LocalDateTime viewedAt;
  private Boolean clicked;
  private LocalDateTime clickedAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public Long getAdvertisementId () {
    return advertisementId;
  }

  public void setAdvertisementId (Long advertisementId) {
    this.advertisementId = advertisementId;
  }

  public Long getUserId () {
    return userId;
  }

  public void setUserId (Long userId) {
    this.userId = userId;
  }

  public Long getMovieId () {
    return movieId;
  }

  public void setMovieId (Long movieId) {
    this.movieId = movieId;
  }

  public Long getEpisodeId () {
    return episodeId;
  }

  public void setEpisodeId (Long episodeId) {
    this.episodeId = episodeId;
  }

  public LocalDateTime getViewedAt () {
    return viewedAt;
  }

  public void setViewedAt (LocalDateTime viewedAt) {
    this.viewedAt = viewedAt;
  }

  public Boolean getClicked () {
    return clicked;
  }

  public void setClicked (Boolean clicked) {
    this.clicked = clicked;
  }

  public LocalDateTime getClickedAt () {
    return clickedAt;
  }

  public void setClickedAt (LocalDateTime clickedAt) {
    this.clickedAt = clickedAt;
  }
}