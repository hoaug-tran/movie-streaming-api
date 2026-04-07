package com.hoaug.movieapi.modules.searchhistory.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.searchhistory.application.dto.response.SearchHistoryResponse;
import com.hoaug.movieapi.modules.searchhistory.application.mapper.SearchHistoryMapper;
import com.hoaug.movieapi.modules.searchhistory.domain.repository.SearchHistoryRepository;

@Component
public class GetMySearchHistoriesUseCase {

  private final SearchHistoryRepository searchHistoryRepository;
  private final SearchHistoryMapper searchHistoryMapper;

  public GetMySearchHistoriesUseCase(SearchHistoryRepository searchHistoryRepository,
      SearchHistoryMapper searchHistoryMapper) {
    this.searchHistoryRepository = searchHistoryRepository;
    this.searchHistoryMapper = searchHistoryMapper;
  }

  public List<SearchHistoryResponse> execute (Long userId) {
    return searchHistoryRepository.findByUserIdOrderBySearchedAtDesc(userId).stream()
        .map(searchHistoryMapper::toResponse).toList();
  }
}