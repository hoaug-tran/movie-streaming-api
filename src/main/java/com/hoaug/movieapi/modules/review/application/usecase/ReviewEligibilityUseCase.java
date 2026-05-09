package com.hoaug.movieapi.modules.review.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.model.Episode;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.watchhistory.domain.model.WatchHistory;
import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;

@Component
public class ReviewEligibilityUseCase {

  private static final double REQUIRED_WATCH_PROGRESS = 0.8;

  private final WatchHistoryRepository watchHistoryRepository;
  private final EpisodeRepository episodeRepository;

  public ReviewEligibilityUseCase(WatchHistoryRepository watchHistoryRepository,
      EpisodeRepository episodeRepository) {
    this.watchHistoryRepository = watchHistoryRepository;
    this.episodeRepository = episodeRepository;
  }

  public void validateCanReview (Long userId, Long movieId) {
    if (!canReview(userId, movieId)) {
      throw new AppException(ErrorCode.REVIEW_WATCH_PROGRESS_NOT_ENOUGH);
    }
  }

  public boolean canReview (Long userId, Long movieId) {
    List<WatchHistory> histories = watchHistoryRepository
        .findByUserIdAndMovieIdOrderByLastWatchedAtDesc(userId, movieId);
    return histories.stream().anyMatch(this::isEligibleHistory);
  }

  private boolean isEligibleHistory (WatchHistory history) {
    if (Boolean.TRUE.equals(history.getIsCompleted())) {
      return true;
    }

    Integer watchedDurationSeconds = history.getWatchedDurationSeconds();
    if (watchedDurationSeconds == null || watchedDurationSeconds <= 0) {
      return false;
    }

    return episodeRepository.findById(history.getEpisodeId())
        .map(Episode::getDurationSeconds)
        .filter(duration -> duration != null && duration > 0)
        .map(duration -> watchedDurationSeconds >= Math.ceil(duration * REQUIRED_WATCH_PROGRESS))
        .orElse(false);
  }
}
