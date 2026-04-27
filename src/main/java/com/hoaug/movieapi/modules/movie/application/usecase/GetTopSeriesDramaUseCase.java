package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieListResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class GetTopSeriesDramaUseCase {

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public GetTopSeriesDramaUseCase(MovieRepository movieRepository, MovieMapper movieMapper) {
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  @Cacheable(value = "movies", key = "'top-series-drama:' + #limit")
  public MovieListResponse execute (int limit) {
    return MovieListResponse.builder()
        .movies(movieRepository.findTopSeriesDrama(limit).stream()
            .map(movieMapper::toSummaryResponse).toList())
        .build();
  }
}
