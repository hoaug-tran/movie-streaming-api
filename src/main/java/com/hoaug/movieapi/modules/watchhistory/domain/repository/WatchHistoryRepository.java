package com.hoaug.movieapi.modules.watchhistory.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.watchhistory.domain.model.WatchHistory;

public interface WatchHistoryRepository {
  Optional<WatchHistory> findByUserIdAndEpisodeId (Long userId, Long episodeId);

  Optional<WatchHistory> findById (Long id);

  List<WatchHistory> findByUserIdOrderByLastWatchedAtDesc (Long userId);

  List<WatchHistory> findIncompleteByUserIdOrderByLastWatchedAtDesc (Long userId);

  List<WatchHistory> findByUserIdAndMovieIdOrderByLastWatchedAtDesc (Long userId, Long movieId);

  WatchHistory save (WatchHistory watchHistory);

  void deleteById (Long id);

  void deleteByUserId (Long userId);
}