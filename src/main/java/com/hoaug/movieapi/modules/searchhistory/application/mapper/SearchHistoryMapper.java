package com.hoaug.movieapi.modules.searchhistory.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.searchhistory.application.dto.response.SearchHistoryResponse;
import com.hoaug.movieapi.modules.searchhistory.domain.model.SearchHistory;

@Component
public class SearchHistoryMapper {

  public SearchHistoryResponse toResponse (SearchHistory searchHistory) {
    SearchHistoryResponse response = new SearchHistoryResponse();
    response.setId(searchHistory.getId());
    response.setKeyword(searchHistory.getKeyword());
    response.setSearchedAt(searchHistory.getSearchedAt());
    return response;
  }
}