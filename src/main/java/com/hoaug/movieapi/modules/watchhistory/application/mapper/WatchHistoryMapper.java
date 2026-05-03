package com.hoaug.movieapi.modules.watchhistory.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieBasicResponse;
import com.hoaug.movieapi.modules.movie.domain.model.Episode;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.ContinueWatchingResponse;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.WatchHistoryResponse;
import com.hoaug.movieapi.modules.watchhistory.domain.model.WatchHistory;

@Component
public class WatchHistoryMapper {

  public WatchHistoryResponse toResponse (WatchHistory watchHistory) {
    return toResponse(watchHistory, null, null);
  }

  public WatchHistoryResponse toResponse (WatchHistory watchHistory, Episode episode,
      MovieBasicResponse movie) {
    WatchHistoryResponse response = new WatchHistoryResponse();
    int duration = episode == null || episode.getDurationSeconds() == null ? 0
        : Math.max(0, episode.getDurationSeconds());
    int stoppedAtSecond = safeInt(watchHistory.getStoppedAtSecond());

    response.setId(watchHistory.getId());
    response.setMovieId(watchHistory.getMovieId());
    response.setEpisodeId(watchHistory.getEpisodeId());
    response.setEpisodeTitle(episode == null ? null : episode.getTitle());
    response.setEpisodeNumber(episode == null ? null : episode.getEpisodeNumber());
    response.setEpisodeDurationSeconds(duration);
    response.setWatchedDurationSeconds(safeInt(watchHistory.getWatchedDurationSeconds()));
    response.setStoppedAtSecond(stoppedAtSecond);
    response.setResumeSecond(duration > 0 ? Math.min(stoppedAtSecond, Math.max(0, duration - 10))
        : stoppedAtSecond);
    response.setProgressPercent(duration > 0 ? Math.min(100.0, stoppedAtSecond * 100.0 / duration)
        : (Boolean.TRUE.equals(watchHistory.getIsCompleted()) ? 100.0 : 0.0));
    response.setIsCompleted(watchHistory.getIsCompleted());
    response.setLastWatchedAt(watchHistory.getLastWatchedAt());
    response.setMovie(movie);
    return response;
  }

  public ContinueWatchingResponse toContinueWatchingResponse (WatchHistory watchHistory) {
    return toContinueWatchingResponse(watchHistory, null);
  }

  public ContinueWatchingResponse toContinueWatchingResponse (WatchHistory watchHistory, Episode episode) {
    ContinueWatchingResponse response = new ContinueWatchingResponse();
    int duration = episode == null || episode.getDurationSeconds() == null ? 0
        : Math.max(0, episode.getDurationSeconds());
    int stoppedAtSecond = safeInt(watchHistory.getStoppedAtSecond());

    response.setMovieId(watchHistory.getMovieId());
    response.setEpisodeId(watchHistory.getEpisodeId());
    response.setEpisodeTitle(episode == null ? null : episode.getTitle());
    response.setEpisodeNumber(episode == null ? null : episode.getEpisodeNumber());
    response.setEpisodeDurationSeconds(duration);
    response.setStoppedAtSecond(stoppedAtSecond);
    response.setWatchedDurationSeconds(safeInt(watchHistory.getWatchedDurationSeconds()));
    response.setResumeSecond(duration > 0 ? Math.min(stoppedAtSecond, Math.max(0, duration - 10))
        : stoppedAtSecond);
    response.setProgressPercent(duration > 0 ? Math.min(100.0, stoppedAtSecond * 100.0 / duration) : 0.0);
    response.setLastWatchedAt(watchHistory.getLastWatchedAt());
    return response;
  }

  private int safeInt (Integer value) {
    return value == null ? 0 : Math.max(0, value);
  }
}