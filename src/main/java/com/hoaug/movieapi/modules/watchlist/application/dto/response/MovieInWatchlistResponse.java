package com.hoaug.movieapi.modules.watchlist.application.dto.response;

public class MovieInWatchlistResponse {
  private Long movieId;
  private Boolean inWatchlist;

  public Long getMovieId () {
    return movieId;
  }

  public void setMovieId (Long movieId) {
    this.movieId = movieId;
  }

  public Boolean getInWatchlist () {
    return inWatchlist;
  }

  public void setInWatchlist (Boolean inWatchlist) {
    this.inWatchlist = inWatchlist;
  }
}