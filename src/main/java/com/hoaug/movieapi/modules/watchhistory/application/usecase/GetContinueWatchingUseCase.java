package com.hoaug.movieapi.modules.watchhistory.application.usecase;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Episode;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.ContinueWatchingResponse;
import com.hoaug.movieapi.modules.watchhistory.application.mapper.WatchHistoryMapper;
import com.hoaug.movieapi.modules.watchhistory.domain.model.WatchHistory;
import com.hoaug.movieapi.modules.watchhistory.domain.repository.WatchHistoryRepository;

@Component
public class GetContinueWatchingUseCase {

  private final WatchHistoryRepository watchHistoryRepository;
  private final WatchHistoryMapper watchHistoryMapper;
  private final MovieRepository movieRepository;
  private final EpisodeRepository episodeRepository;
  private final MovieMapper movieMapper;

  public GetContinueWatchingUseCase(WatchHistoryRepository watchHistoryRepository,
      WatchHistoryMapper watchHistoryMapper, MovieRepository movieRepository,
      EpisodeRepository episodeRepository, MovieMapper movieMapper) {
    this.watchHistoryRepository = watchHistoryRepository;
    this.watchHistoryMapper = watchHistoryMapper;
    this.movieRepository = movieRepository;
    this.episodeRepository = episodeRepository;
    this.movieMapper = movieMapper;
  }

  public List<ContinueWatchingResponse> execute (Long userId) {
    return watchHistoryRepository.findIncompleteByUserIdOrderByLastWatchedAtDesc(userId).stream()
        .map(this::toResponse)
        .flatMap(Optional::stream)
        .toList();
  }

  private Optional<ContinueWatchingResponse> toResponse (WatchHistory history) {
    return episodeRepository.findById(history.getEpisodeId()).flatMap(episode -> toResponse(history, episode));
  }

  private Optional<ContinueWatchingResponse> toResponse (WatchHistory history, Episode episode) {
    return movieRepository.findById(history.getMovieId()).map(movie -> {
      ContinueWatchingResponse response = watchHistoryMapper.toContinueWatchingResponse(history, episode);
      response.setMovie(movieMapper.toBasicResponse(movie));
      return response;
    });
  }
}