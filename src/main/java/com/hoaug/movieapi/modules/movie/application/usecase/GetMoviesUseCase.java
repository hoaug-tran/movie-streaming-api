package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieSummaryResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class GetMoviesUseCase {

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public GetMoviesUseCase(MovieRepository movieRepository, MovieMapper movieMapper) {
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  @Cacheable(cacheNames = "movies", key = "'all_published_movies'")
  public List<MovieSummaryResponse> execute () {
    return movieRepository.findAllPublished().stream().map(movieMapper::toSummaryResponse).toList();
  }
}