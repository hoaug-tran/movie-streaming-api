package com.hoaug.movieapi.modules.watchhistory.application.usecase;

import java.time.LocalDateTime;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.watchhistory.application.dto.request.UpsertWatchHistoryRequest;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.WatchHistoryResponse;
import com.hoaug.movieapi.modules.watchhistory.application.mapper.WatchHistoryMapper;
import com.hoaug.movieapi.modules.watchhistory.domain.model.WatchHistory;
import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;

@Component
public class UpsertWatchHistoryUseCase {

  private final WatchHistoryRepository watchHistoryRepository;
  private final MovieRepository movieRepository;
  private final WatchHistoryMapper watchHistoryMapper;

  public UpsertWatchHistoryUseCase(WatchHistoryRepository watchHistoryRepository,
      MovieRepository movieRepository, WatchHistoryMapper watchHistoryMapper) {
    this.watchHistoryRepository = watchHistoryRepository;
    this.movieRepository = movieRepository;
    this.watchHistoryMapper = watchHistoryMapper;
  }

  @CacheEvict(cacheNames = "watchHistory", key = "'user:' + #userId + ':watchhistories'")
  public WatchHistoryResponse execute (Long userId, UpsertWatchHistoryRequest request) {
    movieRepository.findById(request.getMovieId())
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    WatchHistory watchHistory = watchHistoryRepository
        .findByUserIdAndEpisodeId(userId, request.getEpisodeId()).orElseGet(WatchHistory::new);

    if (watchHistory.getId() == null) {
      watchHistory.setUserId(userId);
      watchHistory.setCreatedAt(LocalDateTime.now());
    }

    watchHistory.setMovieId(request.getMovieId());
    watchHistory.setEpisodeId(request.getEpisodeId());
    watchHistory.setWatchedDurationSeconds(request.getWatchedDurationSeconds());
    watchHistory.setStoppedAtSecond(request.getStoppedAtSecond());
    watchHistory.setIsCompleted(request.getIsCompleted());
    watchHistory.setLastWatchedAt(LocalDateTime.now());
    watchHistory.setUpdatedAt(LocalDateTime.now());

    WatchHistory saved = watchHistoryRepository.save(watchHistory);
    return watchHistoryMapper.toResponse(saved);
  }
}