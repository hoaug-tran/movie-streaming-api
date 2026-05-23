package com.hoaug.movieapi.modules.movie.application.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateMovieStatusRequest {

  @NotNull(message = "Vui lòng chọn trạng thái phim.")
  @JsonAlias("movieStatus")
  private MovieStatus status;

  public MovieStatus getStatus () {
    return status;
  }

  public void setStatus (MovieStatus status) {
    this.status = status;
  }
}
