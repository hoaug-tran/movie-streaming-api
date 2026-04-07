package com.hoaug.movieapi.modules.watchhistory.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.watchhistory.infrastructure.persistence.entity.WatchHistoryEntity;

public interface JpaWatchHistoryRepository extends JpaRepository<WatchHistoryEntity, Long> {
  Optional<WatchHistoryEntity> findByUserIdAndEpisodeId (Long userId, Long episodeId);

  List<WatchHistoryEntity> findByUserIdOrderByLastWatchedAtDesc (Long userId);

  List<WatchHistoryEntity> findByUserIdAndIsCompletedFalseOrderByLastWatchedAtDesc (Long userId);

  List<WatchHistoryEntity> findByUserIdAndMovieIdOrderByLastWatchedAtDesc (Long userId,
      Long movieId);
}