package com.hoaug.movieapi.modules.advertisement.application.dto.request;

import jakarta.validation.constraints.NotNull;

public class CreateAdvertisementViewRequest {

  @NotNull
  private Long advertisementId;

  private Long movieId;
  private Long episodeId;

  public Long getAdvertisementId () {
    return advertisementId;
  }

  public void setAdvertisementId (Long advertisementId) {
    this.advertisementId = advertisementId;
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
}