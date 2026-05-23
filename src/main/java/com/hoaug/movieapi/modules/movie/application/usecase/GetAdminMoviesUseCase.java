package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieListResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieSummaryResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class GetAdminMoviesUseCase {

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public GetAdminMoviesUseCase(MovieRepository movieRepository, MovieMapper movieMapper) {
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  public MovieListResponse execute () {
    List<MovieSummaryResponse> movies = movieRepository.findAll().stream()
        .map(movieMapper::toSummaryResponse).toList();

    return new MovieListResponse(movies);
  }
}