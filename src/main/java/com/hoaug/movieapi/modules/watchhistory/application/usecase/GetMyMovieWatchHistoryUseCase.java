package com.hoaug.movieapi.modules.watchhistory.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.watchhistory.application.dto.response.WatchHistoryResponse;
import com.hoaug.movieapi.modules.watchhistory.application.mapper.WatchHistoryMapper;
import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;

@Component
public class GetMyMovieWatchHistoryUseCase {

  private final WatchHistoryRepository watchHistoryRepository;
  private final WatchHistoryMapper watchHistoryMapper;

  public GetMyMovieWatchHistoryUseCase(WatchHistoryRepository watchHistoryRepository,
      WatchHistoryMapper watchHistoryMapper) {
    this.watchHistoryRepository = watchHistoryRepository;
    this.watchHistoryMapper = watchHistoryMapper;
  }

  public List<WatchHistoryResponse> execute (Long userId, Long movieId) {
    return watchHistoryRepository.findByUserIdAndMovieIdOrderByLastWatchedAtDesc(userId, movieId)
        .stream().map(watchHistoryMapper::toResponse).toList();
  }
} 