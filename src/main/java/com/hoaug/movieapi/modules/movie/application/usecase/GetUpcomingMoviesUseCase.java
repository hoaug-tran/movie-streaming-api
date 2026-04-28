package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieListResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class GetUpcomingMoviesUseCase {

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;
  private final GetMovieCategoriesUseCase getMovieCategoriesUseCase;

  public GetUpcomingMoviesUseCase(MovieRepository movieRepository, MovieMapper movieMapper, GetMovieCategoriesUseCase getMovieCategoriesUseCase) {
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
    this.getMovieCategoriesUseCase = getMovieCategoriesUseCase;
  }

  @Cacheable(value = "movies", key = "'upcoming:' + #limit")
  public MovieListResponse execute (int limit) {
    return MovieListResponse.builder()
        .movies(movieRepository.findUpcoming(limit).stream()
            .map(movie -> movieMapper.toSummaryResponse(movie, getMovieCategoriesUseCase.execute(movie.getId()))).toList())
        .build();
  }
}
