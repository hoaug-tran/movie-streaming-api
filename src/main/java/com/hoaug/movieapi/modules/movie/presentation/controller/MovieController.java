package com.hoaug.movieapi.modules.movie.presentation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.movie.application.dto.response.EpisodeResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieSummaryResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.GetEpisodesByMovieUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieByIdUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieBySlugUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMoviesUseCase;

import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("${api.prefix:/api/v1}/movies")
public class MovieController {

  private final GetMoviesUseCase getMoviesUseCase;
  private final GetMovieByIdUseCase getMovieByIdUseCase;
  private final GetMovieBySlugUseCase getMovieBySlugUseCase;
  private final GetEpisodesByMovieUseCase getEpisodesByMovieUseCase;

  public MovieController(GetMoviesUseCase getMoviesUseCase, GetMovieByIdUseCase getMovieByIdUseCase,
      GetMovieBySlugUseCase getMovieBySlugUseCase,
      GetEpisodesByMovieUseCase getEpisodesByMovieUseCase) {
    this.getMoviesUseCase = getMoviesUseCase;
    this.getMovieByIdUseCase = getMovieByIdUseCase;
    this.getMovieBySlugUseCase = getMovieBySlugUseCase;
    this.getEpisodesByMovieUseCase = getEpisodesByMovieUseCase;
  }

  @GetMapping
  public List<MovieSummaryResponse> getMovies () {
    return getMoviesUseCase.execute();
  }

  @GetMapping("/{id:[0-9]+}")
  public MovieDetailResponse getMovieById (
      @PathVariable @Positive(message = "Movie ID must be positive") Long id) {
    return getMovieByIdUseCase.execute(id);
  }

  @GetMapping("/slug/{slug}")
  public MovieDetailResponse getMovieBySlug (@PathVariable String slug) {
    return getMovieBySlugUseCase.execute(slug);
  }

  @GetMapping("/{id:[0-9]+}/episodes")
  public List<EpisodeResponse> getEpisodes (
      @PathVariable @Positive(message = "Movie ID must be positive") Long id) {
    return getEpisodesByMovieUseCase.execute(id);
  }
}