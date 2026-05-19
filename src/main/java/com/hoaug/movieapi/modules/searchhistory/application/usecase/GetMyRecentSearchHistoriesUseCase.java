package com.hoaug.movieapi.modules.searchhistory.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.searchhistory.application.dto.response.SearchHistoryResponse;
import com.hoaug.movieapi.modules.searchhistory.application.mapper.SearchHistoryMapper;
import com.hoaug.movieapi.modules.searchhistory.domain.repository.SearchHistoryRepository;

@Component
public class GetMyRecentSearchHistoriesUseCase {

  private static final int DEFAULT_LIMIT = 10;
  private static final int MAX_LIMIT = 20;

  private final SearchHistoryRepository searchHistoryRepository;
  private final SearchHistoryMapper searchHistoryMapper;

  public GetMyRecentSearchHistoriesUseCase(SearchHistoryRepository searchHistoryRepository,
      SearchHistoryMapper searchHistoryMapper) {
    this.searchHistoryRepository = searchHistoryRepository;
    this.searchHistoryMapper = searchHistoryMapper;
  }

  public List<SearchHistoryResponse> execute (Long userId, Integer limit) {
    int effectiveLimit = limit == null || limit <= 0 ? DEFAULT_LIMIT : Math.min(limit, MAX_LIMIT);
    return searchHistoryRepository.findRecentByUserId(userId, effectiveLimit).stream()
        .map(searchHistoryMapper::toResponse).toList();
  }
}
