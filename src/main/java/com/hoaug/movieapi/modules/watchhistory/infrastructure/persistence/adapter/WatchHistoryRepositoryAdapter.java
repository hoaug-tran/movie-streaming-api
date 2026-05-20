package com.hoaug.movieapi.modules.watchhistory.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.watchhistory.domain.model.WatchHistory;
import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;
import com.hoaug.movieapi.modules.watchhistory.infrastructure.persistence.entity.WatchHistoryEntity;
import com.hoaug.movieapi.modules.watchhistory.infrastructure.persistence.repository.JpaWatchHistoryRepository;

@Component
public class WatchHistoryRepositoryAdapter implements WatchHistoryRepository {

  private final JpaWatchHistoryRepository jpaWatchHistoryRepository;

  public WatchHistoryRepositoryAdapter(JpaWatchHistoryRepository jpaWatchHistoryRepository) {
    this.jpaWatchHistoryRepository = jpaWatchHistoryRepository;
  }

  @Override
  public Optional<WatchHistory> findByUserIdAndEpisodeId (Long userId, Long episodeId) {
    return jpaWatchHistoryRepository.findByUserIdAndEpisodeId(userId, episodeId)
        .map(this::toDomain);
  }

  @Override
  public Optional<WatchHistory> findById (Long id) {
    return jpaWatchHistoryRepository.findById(id).map(this::toDomain);
  }

  @Override
  public List<WatchHistory> findByUserIdOrderByLastWatchedAtDesc (Long userId) {
    return jpaWatchHistoryRepository.findByUserIdOrderByLastWatchedAtDesc(userId).stream()
        .map(this::toDomain).toList();
  }

  @Override
  public List<WatchHistory> findIncompleteByUserIdOrderByLastWatchedAtDesc (Long userId) {
    return jpaWatchHistoryRepository.findByUserIdAndIsCompletedFalseOrderByLastWatchedAtDesc(userId)
        .stream().map(this::toDomain).toList();
  }

  @Override
  public List<WatchHistory> findByUserIdAndMovieIdOrderByLastWatchedAtDesc (Long userId,
      Long movieId) {
    return jpaWatchHistoryRepository.findByUserIdAndMovieIdOrderByLastWatchedAtDesc(userId, movieId)
        .stream().map(this::toDomain).toList();
  }

  @Override
  public WatchHistory save (WatchHistory watchHistory) {
    WatchHistoryEntity savedEntity = jpaWatchHistoryRepository.save(toEntity(watchHistory));
    return toDomain(savedEntity);
  }

  @Override
  public void deleteById (Long id) {
    jpaWatchHistoryRepository.deleteById(id);
  }

  @Override
  public void deleteByUserId (Long userId) {
    jpaWatchHistoryRepository.deleteByUserId(userId);
  }

  private WatchHistory toDomain (WatchHistoryEntity entity) {
    WatchHistory watchHistory = new WatchHistory();
    watchHistory.setId(entity.getId());
    watchHistory.setUserId(entity.getUserId());
    watchHistory.setMovieId(entity.getMovieId());
    watchHistory.setEpisodeId(entity.getEpisodeId());
    watchHistory.setWatchedDurationSeconds(entity.getWatchedDurationSeconds());
    watchHistory.setStoppedAtSecond(entity.getStoppedAtSecond());
    watchHistory.setIsCompleted(entity.getIsCompleted());
    watchHistory.setLastWatchedAt(entity.getLastWatchedAt());
    watchHistory.setCreatedAt(entity.getCreatedAt());
    watchHistory.setUpdatedAt(entity.getUpdatedAt());
    return watchHistory;
  }

  private WatchHistoryEntity toEntity (WatchHistory watchHistory) {
    WatchHistoryEntity entity = new WatchHistoryEntity();
    entity.setId(watchHistory.getId());
    entity.setUserId(watchHistory.getUserId());
    entity.setMovieId(watchHistory.getMovieId());
    entity.setEpisodeId(watchHistory.getEpisodeId());
    entity.setWatchedDurationSeconds(watchHistory.getWatchedDurationSeconds());
    entity.setStoppedAtSecond(watchHistory.getStoppedAtSecond());
    entity.setIsCompleted(watchHistory.getIsCompleted());
    entity.setLastWatchedAt(watchHistory.getLastWatchedAt());
    entity.setCreatedAt(watchHistory.getCreatedAt());
    entity.setUpdatedAt(watchHistory.getUpdatedAt());
    return entity;
  }
}