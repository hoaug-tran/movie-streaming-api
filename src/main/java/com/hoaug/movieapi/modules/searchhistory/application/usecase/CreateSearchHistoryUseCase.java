package com.hoaug.movieapi.modules.searchhistory.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.searchhistory.application.dto.request.CreateSearchHistoryRequest;
import com.hoaug.movieapi.modules.searchhistory.application.dto.response.SearchHistoryResponse;
import com.hoaug.movieapi.modules.searchhistory.application.mapper.SearchHistoryMapper;
import com.hoaug.movieapi.modules.searchhistory.domain.model.SearchHistory;
import com.hoaug.movieapi.modules.searchhistory.domain.repository.SearchHistoryRepository;

@Component
public class CreateSearchHistoryUseCase {

  private final SearchHistoryRepository searchHistoryRepository;
  private final SearchHistoryMapper searchHistoryMapper;

  public CreateSearchHistoryUseCase(SearchHistoryRepository searchHistoryRepository,
      SearchHistoryMapper searchHistoryMapper) {
    this.searchHistoryRepository = searchHistoryRepository;
    this.searchHistoryMapper = searchHistoryMapper;
  }

  public SearchHistoryResponse execute (Long userId, CreateSearchHistoryRequest request) {
    SearchHistory searchHistory = new SearchHistory();
    searchHistory.setUserId(userId);
    searchHistory.setKeyword(request.getKeyword());
    searchHistory.setSearchedAt(LocalDateTime.now());

    SearchHistory saved = searchHistoryRepository.save(searchHistory);
    return searchHistoryMapper.toResponse(saved);
  }
}