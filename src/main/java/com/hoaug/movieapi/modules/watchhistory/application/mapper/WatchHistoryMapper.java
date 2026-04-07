package com.hoaug.movieapi.modules.watchhistory.application.mapper;

import org.springframework.stereotype.Component;

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
    ContinueWatchingResponse response = new ContinueWatchingResponse();
    response.setMovieId(watchHistory.getMovieId());
    response.setEpisodeId(watchHistory.getEpisodeId());
    response.setStoppedAtSecond(watchHistory.getStoppedAtSecond());
    response.setWatchedDurationSeconds(watchHistory.getWatchedDurationSeconds());
    response.setLastWatchedAt(watchHistory.getLastWatchedAt());
    return response;
  }
}