package com.hoaug.movieapi.modules.watchhistory.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.ContinueWatchingResponse;
import com.hoaug.movieapi.modules.watchhistory.application.mapper.WatchHistoryMapper;
import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;

@Component
public class GetContinueWatchingUseCase {

  private final WatchHistoryRepository watchHistoryRepository;
  private final WatchHistoryMapper watchHistoryMapper;
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public GetContinueWatchingUseCase(WatchHistoryRepository watchHistoryRepository,
      WatchHistoryMapper watchHistoryMapper, MovieRepository movieRepository,
      MovieMapper movieMapper) {
    this.watchHistoryRepository = watchHistoryRepository;
    this.watchHistoryMapper = watchHistoryMapper;
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  public List<ContinueWatchingResponse> execute (Long userId) {
    return watchHistoryRepository.findIncompleteByUserIdOrderByLastWatchedAtDesc(userId).stream()
        .map(history -> {
          ContinueWatchingResponse res = watchHistoryMapper.toContinueWatchingResponse(history);
          movieRepository.findById(history.getMovieId()).ifPresent(movie -> {
            res.setMovie(movieMapper.toBasicResponse(movie));
          });
          return res;
        }).toList();
  }
}