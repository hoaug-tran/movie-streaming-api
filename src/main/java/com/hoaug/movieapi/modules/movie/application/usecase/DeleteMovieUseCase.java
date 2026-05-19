package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class DeleteMovieUseCase {

  private final MovieRepository movieRepository;

  public DeleteMovieUseCase(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  @Caching(evict = {
      @CacheEvict(cacheNames = "movies", allEntries = true),
      @CacheEvict(cacheNames = "movieDetail", key = "#movieId"),
      @CacheEvict(cacheNames = "movieDetailBySlug", allEntries = true),
      @CacheEvict(cacheNames = "searchResults", allEntries = true)
  })
  public void execute (Long movieId) {
    movieRepository.findById(movieId)
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    movieRepository.deleteById(movieId);
  }
}
