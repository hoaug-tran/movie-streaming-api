package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieListResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class GetTrendingMoviesUseCase {

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public GetTrendingMoviesUseCase(MovieRepository movieRepository, MovieMapper movieMapper) {
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  @Cacheable(value = "movies", key = "'trending:' + #limit")
  public MovieListResponse execute (int limit) {
    return MovieListResponse.builder()
        .movies(movieRepository.findTopTrending(limit).stream()
            .map(movieMapper::toSummaryResponse).toList())
        .build();
  }
}
