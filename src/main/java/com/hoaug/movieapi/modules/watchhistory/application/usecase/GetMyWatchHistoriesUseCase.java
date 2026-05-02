package com.hoaug.movieapi.modules.watchhistory.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Episode;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.WatchHistoryListResponse;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.WatchHistoryResponse;
import com.hoaug.movieapi.modules.watchhistory.application.mapper.WatchHistoryMapper;
import com.hoaug.movieapi.modules.watchhistory.domain.model.WatchHistory;
import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;

@Component
public class GetMyWatchHistoriesUseCase {

  private final WatchHistoryRepository watchHistoryRepository;
  private final WatchHistoryMapper watchHistoryMapper;
  private final EpisodeRepository episodeRepository;
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public GetMyWatchHistoriesUseCase(WatchHistoryRepository watchHistoryRepository,
      WatchHistoryMapper watchHistoryMapper, EpisodeRepository episodeRepository,
      MovieRepository movieRepository, MovieMapper movieMapper) {
    this.watchHistoryRepository = watchHistoryRepository;
    this.watchHistoryMapper = watchHistoryMapper;
    this.episodeRepository = episodeRepository;
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  public WatchHistoryListResponse execute (Long userId) {
    List<WatchHistoryResponse> items = watchHistoryRepository.findByUserIdOrderByLastWatchedAtDesc(userId)
        .stream().map(this::toResponse).toList();
    return new WatchHistoryListResponse(items);
  }

  private WatchHistoryResponse toResponse (WatchHistory history) {
    Episode episode = episodeRepository.findById(history.getEpisodeId()).orElse(null);
    return watchHistoryMapper.toResponse(history, episode, movieRepository
        .findById(history.getMovieId()).map(movieMapper::toBasicResponse).orElse(null));
  }
}