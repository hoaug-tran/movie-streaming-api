package com.hoaug.movieapi.modules.watchlist.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.watchlist.application.dto.response.WatchlistListResponse;
import com.hoaug.movieapi.modules.watchlist.application.dto.response.WatchlistResponse;
import com.hoaug.movieapi.modules.watchlist.application.mapper.WatchlistMapper;
import com.hoaug.movieapi.modules.watchlist.domain.model.Watchlist;
import com.hoaug.movieapi.modules.watchlist.domain.repository.WatchlistRepository;

@Component
public class GetMyWatchlistUseCase {

  private final WatchlistRepository watchlistRepository;
  private final WatchlistMapper watchlistMapper;
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public GetMyWatchlistUseCase(WatchlistRepository watchlistRepository,
      WatchlistMapper watchlistMapper, MovieRepository movieRepository, MovieMapper movieMapper) {
    this.watchlistRepository = watchlistRepository;
    this.watchlistMapper = watchlistMapper;
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  public WatchlistListResponse execute (Long userId) {
    List<WatchlistResponse> items = watchlistRepository.findByUserIdOrderByAddedAtDesc(userId)
        .stream().map(this::toResponse).toList();
    return new WatchlistListResponse(items);
  }

  private WatchlistResponse toResponse (Watchlist watchlist) {
    return watchlistMapper.toResponse(watchlist, movieRepository.findById(watchlist.getMovieId())
        .map(movieMapper::toBasicResponse).orElse(null));
  }
}