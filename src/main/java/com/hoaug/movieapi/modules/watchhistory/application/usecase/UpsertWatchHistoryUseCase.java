package com.hoaug.movieapi.modules.watchhistory.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.model.Episode;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.watchhistory.application.dto.request.UpsertWatchHistoryRequest;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.WatchHistoryResponse;
import com.hoaug.movieapi.modules.watchhistory.application.mapper.WatchHistoryMapper;
import com.hoaug.movieapi.modules.watchhistory.domain.model.WatchHistory;
import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;

@Component
public class UpsertWatchHistoryUseCase {

  private static final int COMPLETION_GRACE_SECONDS = 10;
  private static final double COMPLETION_PERCENT = 0.95;

  private final WatchHistoryRepository watchHistoryRepository;
  private final MovieRepository movieRepository;
  private final EpisodeRepository episodeRepository;
  private final WatchHistoryMapper watchHistoryMapper;

  public UpsertWatchHistoryUseCase(WatchHistoryRepository watchHistoryRepository,
      MovieRepository movieRepository, EpisodeRepository episodeRepository,
      WatchHistoryMapper watchHistoryMapper) {
    this.watchHistoryRepository = watchHistoryRepository;
    this.movieRepository = movieRepository;
    this.episodeRepository = episodeRepository;
    this.watchHistoryMapper = watchHistoryMapper;
  }

  public WatchHistoryResponse execute (Long userId, UpsertWatchHistoryRequest request) {
    movieRepository.findById(request.getMovieId())
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    Episode episode = episodeRepository.findById(request.getEpisodeId())
        .orElseThrow( () -> new AppException(ErrorCode.EPISODE_NOT_FOUND));

    if (!request.getMovieId().equals(episode.getMovieId())) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    int duration = safeDuration(episode.getDurationSeconds());
    int stoppedAtSecond = clamp(request.getStoppedAtSecond(), 0, duration);
    int watchedDurationSeconds = clamp(request.getWatchedDurationSeconds(), 0, duration);
    boolean isCompleted = isCompleted(request.getIsCompleted(), stoppedAtSecond, duration);

    if (isCompleted && duration > 0) {
      stoppedAtSecond = duration;
      watchedDurationSeconds = Math.max(watchedDurationSeconds, duration);
    }

    WatchHistory watchHistory = watchHistoryRepository
        .findByUserIdAndEpisodeId(userId, request.getEpisodeId()).orElseGet(WatchHistory::new);

    LocalDateTime now = LocalDateTime.now();
    boolean isNew = watchHistory.getId() == null;

    if (isNew) {
      watchHistory.setUserId(userId);
      watchHistory.setCreatedAt(now);
      movieRepository.incrementViewCount(request.getMovieId());
    }

    watchHistory.setMovieId(request.getMovieId());
    watchHistory.setEpisodeId(request.getEpisodeId());
    watchHistory.setWatchedDurationSeconds(watchedDurationSeconds);
    watchHistory.setStoppedAtSecond(stoppedAtSecond);
    watchHistory.setIsCompleted(isCompleted);
    watchHistory.setLastWatchedAt(now);
    watchHistory.setUpdatedAt(now);

    WatchHistory saved = watchHistoryRepository.save(watchHistory);
    return watchHistoryMapper.toResponse(saved);
  }

  private int safeDuration (Integer duration) {
    return duration == null || duration < 0 ? 0 : duration;
  }

  private int clamp (Integer value, int min, int max) {
    int safeValue = value == null ? min : value;
    if (max <= min) {
      return Math.max(min, safeValue);
    }
    return Math.max(min, Math.min(safeValue, max));
  }

  private boolean isCompleted (Boolean clientCompleted, int stoppedAtSecond, int duration) {
    if (duration <= 0) {
      return Boolean.TRUE.equals(clientCompleted);
    }
    return Boolean.TRUE.equals(clientCompleted)
        || stoppedAtSecond >= duration - COMPLETION_GRACE_SECONDS
        || stoppedAtSecond >= Math.floor(duration * COMPLETION_PERCENT);
  }
}