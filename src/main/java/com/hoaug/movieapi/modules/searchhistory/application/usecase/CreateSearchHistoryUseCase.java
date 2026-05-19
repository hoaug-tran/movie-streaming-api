package com.hoaug.movieapi.modules.searchhistory.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.modules.searchhistory.application.dto.request.CreateSearchHistoryRequest;
import com.hoaug.movieapi.modules.searchhistory.application.dto.response.SearchHistoryResponse;
import com.hoaug.movieapi.modules.searchhistory.application.mapper.SearchHistoryMapper;
import com.hoaug.movieapi.modules.searchhistory.domain.model.SearchHistory;
import com.hoaug.movieapi.modules.searchhistory.domain.repository.SearchHistoryRepository;

@Component
public class CreateSearchHistoryUseCase {

  private static final int MAX_HISTORIES_PER_USER = 20;

  private final SearchHistoryRepository searchHistoryRepository;
  private final SearchHistoryMapper searchHistoryMapper;

  public CreateSearchHistoryUseCase(SearchHistoryRepository searchHistoryRepository,
      SearchHistoryMapper searchHistoryMapper) {
    this.searchHistoryRepository = searchHistoryRepository;
    this.searchHistoryMapper = searchHistoryMapper;
  }

  @Transactional
  public SearchHistoryResponse execute (Long userId, CreateSearchHistoryRequest request) {
    return searchHistoryMapper
        .toResponse(persist(userId, request != null ? request.getKeyword() : null));
  }

  @Transactional
  public void recordKeyword (Long userId, String rawKeyword) {
    if (userId == null) {
      return;
    }
    persist(userId, rawKeyword);
  }

  private SearchHistory persist (Long userId, String rawKeyword) {
    String keyword = normalize(rawKeyword);
    if (keyword == null) {
      return null;
    }

    SearchHistory record = searchHistoryRepository.findByUserIdAndKeyword(userId, keyword)
        .map(existing -> {
          existing.setSearchedAt(LocalDateTime.now());
          existing.setKeyword(keyword);
          return existing;
        }).orElseGet( () -> {
          SearchHistory created = new SearchHistory();
          created.setUserId(userId);
          created.setKeyword(keyword);
          created.setSearchedAt(LocalDateTime.now());
          return created;
        });

    SearchHistory saved = searchHistoryRepository.save(record);

    if (searchHistoryRepository.countByUserId(userId) > MAX_HISTORIES_PER_USER) {
      searchHistoryRepository.trimUserHistoryToLimit(userId, MAX_HISTORIES_PER_USER);
    }

    return saved;
  }

  private String normalize (String value) {
    if (value == null) {
      return null;
    }
    String trimmed = value.trim().replaceAll("\\s+", " ");
    if (trimmed.isEmpty()) {
      return null;
    }
    return trimmed.length() > 255 ? trimmed.substring(0, 255) : trimmed;
  }
}