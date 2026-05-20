package com.hoaug.movieapi.modules.watchhistory.application.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.watchhistory.domain.model.WatchHistory;
import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;

@Component
public class DeleteWatchHistoryUseCase {

  private final WatchHistoryRepository watchHistoryRepository;

  public DeleteWatchHistoryUseCase(WatchHistoryRepository watchHistoryRepository) {
    this.watchHistoryRepository = watchHistoryRepository;
  }

  @Transactional
  public void execute (Long userId, Long historyId) {
    WatchHistory watchHistory = watchHistoryRepository.findById(historyId)
        .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST));
    if (!userId.equals(watchHistory.getUserId())) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }
    watchHistoryRepository.deleteById(historyId);
  }
}
