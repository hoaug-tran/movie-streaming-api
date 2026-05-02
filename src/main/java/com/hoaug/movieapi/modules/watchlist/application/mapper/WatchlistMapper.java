package com.hoaug.movieapi.modules.watchlist.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieBasicResponse;
import com.hoaug.movieapi.modules.watchlist.application.dto.response.WatchlistResponse;
import com.hoaug.movieapi.modules.watchlist.domain.model.Watchlist;

@Component
public class WatchlistMapper {

  public WatchlistResponse toResponse (Watchlist watchlist) {
    return toResponse(watchlist, null);
  }

  public WatchlistResponse toResponse (Watchlist watchlist, MovieBasicResponse movie) {
    WatchlistResponse response = new WatchlistResponse();
    response.setId(watchlist.getId());
    response.setMovieId(watchlist.getMovieId());
    response.setAddedAt(watchlist.getAddedAt());
    response.setMovie(movie);
    return response;
  }
}