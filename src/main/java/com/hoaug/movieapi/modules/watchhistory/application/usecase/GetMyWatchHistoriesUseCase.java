package com.hoaug.movieapi.modules.watchhistory.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.watchhistory.application.dto.response.WatchHistoryListResponse;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.WatchHistoryResponse;
import com.hoaug.movieapi.modules.watchhistory.application.mapper.WatchHistoryMapper;
import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;

@Component
public class GetMyWatchHistoriesUseCase {

  private final WatchHistoryRepository watchHistoryRepository;
  private final WatchHistoryMapper watchHistoryMapper;

  public GetMyWatchHistoriesUseCase(WatchHistoryRepository watchHistoryRepository,
      WatchHistoryMapper watchHistoryMapper) {
    this.watchHistoryRepository = watchHistoryRepository;
    this.watchHistoryMapper = watchHistoryMapper;
  }

  public WatchHistoryListResponse execute (Long userId) {
    List<WatchHistoryResponse> items = watchHistoryRepository.findByUserIdOrderByLastWatchedAtDesc(userId)
        .stream().map(watchHistoryMapper::toResponse).toList();
    return new WatchHistoryListResponse(items);
  }
}