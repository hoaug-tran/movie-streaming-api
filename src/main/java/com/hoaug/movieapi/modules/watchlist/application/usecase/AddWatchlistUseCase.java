package com.hoaug.movieapi.modules.watchlist.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.watchlist.application.dto.response.WatchlistResponse;
import com.hoaug.movieapi.modules.watchlist.application.mapper.WatchlistMapper;
import com.hoaug.movieapi.modules.watchlist.domain.model.Watchlist;
import com.hoaug.movieapi.modules.watchlist.domain.repository.WatchlistRepository;

@Component
public class AddWatchlistUseCase {

  private final WatchlistRepository watchlistRepository;
  private final MovieRepository movieRepository;
  private final WatchlistMapper watchlistMapper;

  public AddWatchlistUseCase(WatchlistRepository watchlistRepository,
      MovieRepository movieRepository, WatchlistMapper watchlistMapper) {
    this.watchlistRepository = watchlistRepository;
    this.movieRepository = movieRepository;
    this.watchlistMapper = watchlistMapper;
  }

  public WatchlistResponse execute (Long userId, Long movieId) {
    movieRepository.findById(movieId)
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    Watchlist watchlist = watchlistRepository.findByUserIdAndMovieId(userId, movieId)
        .orElseGet( () -> {
          Watchlist item = new Watchlist();
          item.setUserId(userId);
          item.setMovieId(movieId);
          item.setAddedAt(LocalDateTime.now());
          return item;
        });

    Watchlist saved = watchlistRepository.save(watchlist);
    return watchlistMapper.toResponse(saved);
  }
}