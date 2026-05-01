package com.hoaug.movieapi.modules.watchhistory.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.domain.model.Episode;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.ContinueWatchingResponse;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.WatchHistoryResponse;
import com.hoaug.movieapi.modules.watchhistory.domain.model.WatchHistory;

@Component
public class WatchHistoryMapper {

  public WatchHistoryResponse toResponse (WatchHistory watchHistory) {
    WatchHistoryResponse response = new WatchHistoryResponse();
    response.setId(watchHistory.getId());
    response.setMovieId(watchHistory.getMovieId());
    response.setEpisodeId(watchHistory.getEpisodeId());
    response.setWatchedDurationSeconds(watchHistory.getWatchedDurationSeconds());
    response.setStoppedAtSecond(watchHistory.getStoppedAtSecond());
    response.setIsCompleted(watchHistory.getIsCompleted());
    response.setLastWatchedAt(watchHistory.getLastWatchedAt());
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