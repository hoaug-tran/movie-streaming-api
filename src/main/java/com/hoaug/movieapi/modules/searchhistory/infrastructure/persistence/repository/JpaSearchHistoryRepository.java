package com.hoaug.movieapi.modules.searchhistory.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.searchhistory.infrastructure.persistence.entity.SearchHistoryEntity;

public interface JpaSearchHistoryRepository extends JpaRepository<SearchHistoryEntity, Long> {

  List<SearchHistoryEntity> findByUserIdOrderBySearchedAtDesc (Long userId);

  void deleteAllByUserId (Long userId);
}