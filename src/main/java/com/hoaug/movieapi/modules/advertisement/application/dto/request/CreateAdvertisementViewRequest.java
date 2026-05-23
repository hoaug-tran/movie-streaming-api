package com.hoaug.movieapi.modules.advertisement.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateAdvertisementViewRequest {

  @NotNull(message = "Advertisement ID is required")
  @Positive(message = "Advertisement ID must be positive")
  private Long advertisementId;

  @Positive(message = "ID phim phải là số dương. if provided")
  private Long movieId;

  @Positive(message = "ID tập phim phải là số dương. if provided")
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