package com.hoaug.movieapi.modules.searchhistory.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.searchhistory.infrastructure.persistence.entity.SearchHistoryEntity;

public interface JpaSearchHistoryRepository extends JpaRepository<SearchHistoryEntity, Long> {

  List<SearchHistoryEntity> findByUserIdOrderBySearchedAtDesc (Long userId);

  List<SearchHistoryEntity> findByUserIdOrderBySearchedAtDesc (Long userId, Pageable pageable);

  List<SearchHistoryEntity> findByUserIdOrderBySearchedAtAscIdAsc (Long userId, Pageable pageable);

  Optional<SearchHistoryEntity> findFirstByUserIdAndKeywordIgnoreCase (Long userId, String keyword);

  long countByUserId (Long userId);

  void deleteAllByUserId (Long userId);
}