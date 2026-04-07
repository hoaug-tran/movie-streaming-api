package com.hoaug.movieapi.modules.watchlist.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.watchlist.domain.repository.WatchlistRepository;

@Component
public class RemoveWatchlistUseCase {

  private final WatchlistRepository watchlistRepository;

  public RemoveWatchlistUseCase(WatchlistRepository watchlistRepository) {
    this.watchlistRepository = watchlistRepository;
  }

  public void execute (Long userId, Long movieId) {
    watchlistRepository.findByUserIdAndMovieId(userId, movieId)
        .ifPresent(watchlistRepository::delete);
  }
}