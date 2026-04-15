package com.hoaug.movieapi.modules.watchlist.application.usecase;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.watchlist.application.dto.response.WatchlistResponse;
import com.hoaug.movieapi.modules.watchlist.application.mapper.WatchlistMapper;
import com.hoaug.movieapi.modules.watchlist.domain.repository.WatchlistRepository;

@Component
public class GetMyWatchlistUseCase {

  private final WatchlistRepository watchlistRepository;
  private final WatchlistMapper watchlistMapper;

  public GetMyWatchlistUseCase(WatchlistRepository watchlistRepository,
      WatchlistMapper watchlistMapper) {
    this.watchlistRepository = watchlistRepository;
    this.watchlistMapper = watchlistMapper;
  }

  @Cacheable(cacheNames = "watchlist", key = "'user:' + #userId + ':watchlist'")
  public List<WatchlistResponse> execute (Long userId) {
    return watchlistRepository.findByUserIdOrderByAddedAtDesc(userId).stream()
        .map(watchlistMapper::toResponse).toList();
  }
}