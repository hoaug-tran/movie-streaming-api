package com.hoaug.movieapi.modules.watchlist.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.watchlist.application.dto.response.MovieInWatchlistResponse;
import com.hoaug.movieapi.modules.watchlist.domain.repository.WatchlistRepository;

@Component
public class CheckMovieInWatchlistUseCase {

  private final WatchlistRepository watchlistRepository;

  public CheckMovieInWatchlistUseCase(WatchlistRepository watchlistRepository) {
    this.watchlistRepository = watchlistRepository;
  }

  public MovieInWatchlistResponse execute (Long userId, Long movieId) {
    MovieInWatchlistResponse response = new MovieInWatchlistResponse();
    response.setMovieId(movieId);
    response
        .setInWatchlist(watchlistRepository.findByUserIdAndMovieId(userId, movieId).isPresent());
    return response;
  }
}