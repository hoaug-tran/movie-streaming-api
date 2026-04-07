package com.hoaug.movieapi.modules.watchhistory.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.watchhistory.application.dto.response.ContinueWatchingResponse;
import com.hoaug.movieapi.modules.watchhistory.application.mapper.WatchHistoryMapper;
import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;

@Component
public class GetContinueWatchingUseCase {

  private final WatchHistoryRepository watchHistoryRepository;
  private final WatchHistoryMapper watchHistoryMapper;

  public GetContinueWatchingUseCase(WatchHistoryRepository watchHistoryRepository,
      WatchHistoryMapper watchHistoryMapper) {
    this.watchHistoryRepository = watchHistoryRepository;
    this.watchHistoryMapper = watchHistoryMapper;
  }

  public List<ContinueWatchingResponse> execute (Long userId) {
    return watchHistoryRepository.findIncompleteByUserIdOrderByLastWatchedAtDesc(userId).stream()
        .map(watchHistoryMapper::toContinueWatchingResponse).toList();
  }
}