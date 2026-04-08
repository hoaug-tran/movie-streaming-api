package com.hoaug.movieapi.modules.movie.application.dto.request;

import com.hoaug.movieapi.modules.movie.domain.model.MovieStatus;

public class UpdateMovieStatusRequest {

  private MovieStatus status;

  public MovieStatus getStatus () {
    return status;
  }

  public void setStatus (MovieStatus status) {
    this.status = status;
  }
}
