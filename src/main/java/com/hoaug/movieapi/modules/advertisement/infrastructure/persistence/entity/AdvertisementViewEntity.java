package com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "advertisement_views")
public class AdvertisementViewEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "advertisement_id", nullable = false)
  private Long advertisementId;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "movie_id")
  private Long movieId;

  @Column(name = "episode_id")
  private Long episodeId;

  @Column(name = "viewed_at", nullable = false)
  private LocalDateTime viewedAt;

  @Column(nullable = false)
  private Boolean clicked;

  @Column(name = "clicked_at")
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