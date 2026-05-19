package com.hoaug.movieapi.modules.movie.application.usecase;

import java.time.LocalDateTime;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateEpisodeRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.EpisodeResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Episode;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class CreateEpisodeUseCase {

  private final MovieRepository movieRepository;
  private final EpisodeRepository episodeRepository;
  private final MovieMapper movieMapper;

  public CreateEpisodeUseCase(MovieRepository movieRepository, EpisodeRepository episodeRepository,
      MovieMapper movieMapper) {
    this.movieRepository = movieRepository;
    this.episodeRepository = episodeRepository;
    this.movieMapper = movieMapper;
  }

  @Caching(evict = {
      @CacheEvict(cacheNames = "movieDetail", key = "#movieId"),
      @CacheEvict(cacheNames = "movieDetailBySlug", allEntries = true)
  })
  public EpisodeResponse execute (Long movieId, CreateEpisodeRequest request) {
    movieRepository.findById(movieId)
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    Episode episode = new Episode();
    episode.setMovieId(movieId);
    episode.setTitle(request.getTitle());
    episode.setEpisodeNumber(request.getEpisodeNumber());
    episode.setVideoUrl(request.getVideoUrl() != null && !request.getVideoUrl().isBlank()
        ? request.getVideoUrl() : "pending");
    episode.setThumbnailUrl(request.getThumbnailUrl());
    episode.setDurationSeconds(request.getDurationSeconds());
    episode.setIsFreePreview(request.getIsFreePreview());
    episode.setStatus(request.getStatus());
    episode.setCreatedAt(LocalDateTime.now());
    episode.setUpdatedAt(LocalDateTime.now());

    Episode savedEpisode = episodeRepository.save(episode);
    return movieMapper.toEpisodeResponse(savedEpisode);
  }
}