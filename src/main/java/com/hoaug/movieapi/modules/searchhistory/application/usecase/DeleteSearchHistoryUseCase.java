package com.hoaug.movieapi.modules.searchhistory.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.searchhistory.domain.model.SearchHistory;
import com.hoaug.movieapi.modules.searchhistory.domain.repository.SearchHistoryRepository;

@Component
public class DeleteSearchHistoryUseCase {

  private final SearchHistoryRepository searchHistoryRepository;

  public DeleteSearchHistoryUseCase(SearchHistoryRepository searchHistoryRepository) {
    this.searchHistoryRepository = searchHistoryRepository;
  }

  public void execute (Long userId, Long searchHistoryId) {
    SearchHistory searchHistory = searchHistoryRepository.findById(searchHistoryId)
        .orElseThrow( () -> new AppException(ErrorCode.SEARCH_HISTORY_NOT_FOUND));

    if (!searchHistory.getUserId().equals(userId)) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    searchHistoryRepository.delete(searchHistory);
  }
}