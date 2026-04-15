package com.hoaug.movieapi.modules.watchlist.application.usecase;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.watchlist.domain.repository.WatchlistRepository;

@Component
public class RemoveWatchlistUseCase {

  private final WatchlistRepository watchlistRepository;

  public RemoveWatchlistUseCase(WatchlistRepository watchlistRepository) {
    this.watchlistRepository = watchlistRepository;
  }

  @CacheEvict(cacheNames = "watchlist", key = "'user:' + #userId + ':watchlist'")
  public void execute (Long userId, Long movieId) {
    watchlistRepository.findByUserIdAndMovieId(userId, movieId)
        .ifPresent(watchlistRepository::delete);
  }
}