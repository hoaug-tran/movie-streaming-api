package com.hoaug.movieapi.modules.watchhistory.application.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;

@Component
public class ClearMyWatchHistoryUseCase {

  private final WatchHistoryRepository watchHistoryRepository;

  public ClearMyWatchHistoryUseCase(WatchHistoryRepository watchHistoryRepository) {
    this.watchHistoryRepository = watchHistoryRepository;
  }

  @Transactional
  public void execute (Long userId) {
    watchHistoryRepository.deleteByUserId(userId);
  }
}
