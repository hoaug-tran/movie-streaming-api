package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class DeleteEpisodeUseCase {

  private final MovieRepository movieRepository;
  private final EpisodeRepository episodeRepository;

  public DeleteEpisodeUseCase(MovieRepository movieRepository,
      EpisodeRepository episodeRepository) {
    this.movieRepository = movieRepository;
    this.episodeRepository = episodeRepository;
  }

  @Caching(evict = {
      @CacheEvict(cacheNames = "movieDetail", key = "#movieId"),
      @CacheEvict(cacheNames = "movieDetailBySlug", allEntries = true)
  })
  public void execute (Long movieId, Long episodeId) {
    movieRepository.findById(movieId)
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    episodeRepository.deleteById(episodeId);
  }
}
