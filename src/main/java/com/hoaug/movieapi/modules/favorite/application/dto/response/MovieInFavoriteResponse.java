package com.hoaug.movieapi.modules.favorite.application.dto.response;

public class MovieInFavoriteResponse {
  private Long movieId;
  private Boolean inFavorite;

  public Long getMovieId () {
    return movieId;
  }

  public void setMovieId (Long movieId) {
    this.movieId = movieId;
  }

  public Boolean getInFavorite () {
    return inFavorite;
  }

  public void setInFavorite (Boolean inFavorite) {
    this.inFavorite = inFavorite;
  }
}