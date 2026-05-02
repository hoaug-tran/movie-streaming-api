package com.hoaug.movieapi.modules.watchlist.application.dto.response;

import java.time.LocalDateTime;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieBasicResponse;

public class WatchlistResponse {
  private Long id;
  private Long movieId;
  private LocalDateTime addedAt;
  private MovieBasicResponse movie;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public Long getMovieId () {
    return movieId;
  }

  public void setMovieId (Long movieId) {
    this.movieId = movieId;
  }

  public LocalDateTime getAddedAt () {
    return addedAt;
  }

  public void setAddedAt (LocalDateTime addedAt) {
    this.addedAt = addedAt;
  }

  public MovieBasicResponse getMovie () {
    return movie;
  }

  public void setMovie (MovieBasicResponse movie) {
    this.movie = movie;
  }
}