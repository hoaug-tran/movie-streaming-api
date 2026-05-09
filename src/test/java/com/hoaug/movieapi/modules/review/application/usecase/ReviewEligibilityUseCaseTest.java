package com.hoaug.movieapi.modules.review.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.model.Episode;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.watchhistory.domain.model.WatchHistory;
import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;

class ReviewEligibilityUseCaseTest {

  private WatchHistoryRepository watchHistoryRepository;
  private EpisodeRepository episodeRepository;
  private ReviewEligibilityUseCase useCase;

  @BeforeEach
  void setUp() {
    watchHistoryRepository = mock(WatchHistoryRepository.class);
    episodeRepository = mock(EpisodeRepository.class);
    useCase = new ReviewEligibilityUseCase(watchHistoryRepository, episodeRepository);
  }

  @Test
  void validateAllowsReviewWhenAnyEpisodeProgressReachesEightyPercent() {
    WatchHistory history = history(10L, 480, false);
    Episode episode = episode(10L, 600);

    when(watchHistoryRepository.findByUserIdAndMovieIdOrderByLastWatchedAtDesc(1L, 2L))
        .thenReturn(List.of(history));
    when(episodeRepository.findById(10L)).thenReturn(Optional.of(episode));

    assertDoesNotThrow(() -> useCase.validateCanReview(1L, 2L));
  }

  @Test
  void validateRejectsReviewWhenProgressIsBelowEightyPercent() {
    WatchHistory history = history(10L, 479, false);
    Episode episode = episode(10L, 600);

    when(watchHistoryRepository.findByUserIdAndMovieIdOrderByLastWatchedAtDesc(1L, 2L))
        .thenReturn(List.of(history));
    when(episodeRepository.findById(10L)).thenReturn(Optional.of(episode));

    AppException exception = assertThrows(AppException.class, () -> useCase.validateCanReview(1L, 2L));
    org.junit.jupiter.api.Assertions.assertEquals(ErrorCode.REVIEW_WATCH_PROGRESS_NOT_ENOUGH,
        exception.getErrorCode());
  }

  @Test
  void validateAllowsCompletedHistoryEvenWhenDurationIsMissing() {
    WatchHistory history = history(10L, 1, true);

    when(watchHistoryRepository.findByUserIdAndMovieIdOrderByLastWatchedAtDesc(1L, 2L))
        .thenReturn(List.of(history));
    when(episodeRepository.findById(10L)).thenReturn(Optional.empty());

    assertDoesNotThrow(() -> useCase.validateCanReview(1L, 2L));
  }

  private WatchHistory history(Long episodeId, Integer watchedDurationSeconds, Boolean isCompleted) {
    WatchHistory history = new WatchHistory();
    history.setEpisodeId(episodeId);
    history.setWatchedDurationSeconds(watchedDurationSeconds);
    history.setStoppedAtSecond(watchedDurationSeconds);
    history.setIsCompleted(isCompleted);
    return history;
  }

  private Episode episode(Long id, Integer durationSeconds) {
    Episode episode = new Episode();
    episode.setId(id);
    episode.setDurationSeconds(durationSeconds);
    return episode;
  }
}
