package com.hoaug.movieapi.modules.searchhistory.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.searchhistory.domain.repository.SearchHistoryRepository;

@Component
public class ClearMySearchHistoriesUseCase {

  private final SearchHistoryRepository searchHistoryRepository;

  public ClearMySearchHistoriesUseCase(SearchHistoryRepository searchHistoryRepository) {
    this.searchHistoryRepository = searchHistoryRepository;
  }

  public void execute (Long userId) {
    searchHistoryRepository.deleteAllByUserId(userId);
  }
}