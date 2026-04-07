package com.hoaug.movieapi.modules.searchhistory.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.searchhistory.domain.model.SearchHistory;

public interface SearchHistoryRepository {

  Optional<SearchHistory> findById (Long id);

  SearchHistory save (SearchHistory searchHistory);

  List<SearchHistory> findByUserIdOrderBySearchedAtDesc (Long userId);

  void delete (SearchHistory searchHistory);

  void deleteAllByUserId (Long userId);
}