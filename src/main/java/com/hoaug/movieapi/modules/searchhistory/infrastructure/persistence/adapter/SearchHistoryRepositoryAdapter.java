package com.hoaug.movieapi.modules.searchhistory.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.searchhistory.domain.model.SearchHistory;
import com.hoaug.movieapi.modules.searchhistory.domain.repository.SearchHistoryRepository;
import com.hoaug.movieapi.modules.searchhistory.infrastructure.persistence.entity.SearchHistoryEntity;
import com.hoaug.movieapi.modules.searchhistory.infrastructure.persistence.repository.JpaSearchHistoryRepository;

@Component
public class SearchHistoryRepositoryAdapter implements SearchHistoryRepository {

  private final JpaSearchHistoryRepository jpaSearchHistoryRepository;

  public SearchHistoryRepositoryAdapter(JpaSearchHistoryRepository jpaSearchHistoryRepository) {
    this.jpaSearchHistoryRepository = jpaSearchHistoryRepository;
  }

  @Override
  public Optional<SearchHistory> findById (Long id) {
    return jpaSearchHistoryRepository.findById(id).map(this::toDomain);
  }

  @Override
  public SearchHistory save (SearchHistory searchHistory) {
    SearchHistoryEntity savedEntity = jpaSearchHistoryRepository.save(toEntity(searchHistory));
    return toDomain(savedEntity);
  }

  @Override
  public List<SearchHistory> findByUserIdOrderBySearchedAtDesc (Long userId) {
    return jpaSearchHistoryRepository.findByUserIdOrderBySearchedAtDesc(userId).stream()
        .map(this::toDomain).toList();
  }

  @Override
  public void delete (SearchHistory searchHistory) {
    jpaSearchHistoryRepository.delete(toEntity(searchHistory));
  }

  @Override
  public void deleteAllByUserId (Long userId) {
    jpaSearchHistoryRepository.deleteAllByUserId(userId);
  }

  private SearchHistory toDomain (SearchHistoryEntity entity) {
    SearchHistory searchHistory = new SearchHistory();
    searchHistory.setId(entity.getId());
    searchHistory.setUserId(entity.getUserId());
    searchHistory.setKeyword(entity.getKeyword());
    searchHistory.setSearchedAt(entity.getSearchedAt());
    return searchHistory;
  }

  private SearchHistoryEntity toEntity (SearchHistory searchHistory) {
    SearchHistoryEntity entity = new SearchHistoryEntity();
    entity.setId(searchHistory.getId());
    entity.setUserId(searchHistory.getUserId());
    entity.setKeyword(searchHistory.getKeyword());
    entity.setSearchedAt(searchHistory.getSearchedAt());
    return entity;
  }
}