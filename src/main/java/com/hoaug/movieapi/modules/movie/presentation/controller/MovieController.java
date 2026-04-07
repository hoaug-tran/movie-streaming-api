package com.hoaug.movieapi.modules.movie.presentation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.EpisodeResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MoviePersonResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieStudioResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieSummaryResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.PersonResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.StudioResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.GetEpisodesByMovieUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieByIdUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieBySlugUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieCategoriesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMoviePersonsUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieStudiosUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieTagsUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMoviesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetPersonByIdUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetPersonsUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetStudioByIdUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetStudiosUseCase;

import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("${api.prefix:/api/v1}/movies")
public class MovieController {

  private final GetMoviesUseCase getMoviesUseCase;
  private final GetMovieByIdUseCase getMovieByIdUseCase;
  private final GetMovieBySlugUseCase getMovieBySlugUseCase;
  private final GetEpisodesByMovieUseCase getEpisodesByMovieUseCase;
  private final GetMovieCategoriesUseCase getMovieCategoriesUseCase;
  private final GetMovieTagsUseCase getMovieTagsUseCase;
  private final GetMoviePersonsUseCase getMoviePersonsUseCase;
  private final GetMovieStudiosUseCase getMovieStudiosUseCase;
  private final GetPersonsUseCase getPersonsUseCase;
  private final GetPersonByIdUseCase getPersonByIdUseCase;
  private final GetStudiosUseCase getStudiosUseCase;
  private final GetStudioByIdUseCase getStudioByIdUseCase;

  public MovieController(GetMoviesUseCase getMoviesUseCase, GetMovieByIdUseCase getMovieByIdUseCase,
      GetMovieBySlugUseCase getMovieBySlugUseCase,
      GetEpisodesByMovieUseCase getEpisodesByMovieUseCase,
      GetMovieCategoriesUseCase getMovieCategoriesUseCase, GetMovieTagsUseCase getMovieTagsUseCase,
      GetMoviePersonsUseCase getMoviePersonsUseCase, GetMovieStudiosUseCase getMovieStudiosUseCase,
      GetPersonsUseCase getPersonsUseCase, GetPersonByIdUseCase getPersonByIdUseCase,
      GetStudiosUseCase getStudiosUseCase, GetStudioByIdUseCase getStudioByIdUseCase) {
    this.getMoviesUseCase = getMoviesUseCase;
    this.getMovieByIdUseCase = getMovieByIdUseCase;
    this.getMovieBySlugUseCase = getMovieBySlugUseCase;
    this.getEpisodesByMovieUseCase = getEpisodesByMovieUseCase;
    this.getMovieCategoriesUseCase = getMovieCategoriesUseCase;
    this.getMovieTagsUseCase = getMovieTagsUseCase;
    this.getMoviePersonsUseCase = getMoviePersonsUseCase;
    this.getMovieStudiosUseCase = getMovieStudiosUseCase;
    this.getPersonsUseCase = getPersonsUseCase;
    this.getPersonByIdUseCase = getPersonByIdUseCase;
    this.getStudiosUseCase = getStudiosUseCase;
    this.getStudioByIdUseCase = getStudioByIdUseCase;
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

  @GetMapping("/{id:[0-9]+}/categories")
  public List<CategoryResponse> getMovieCategories (
      @PathVariable @Positive(message = "Movie ID must be positive") Long id) {
    return getMovieCategoriesUseCase.execute(id);
  }

  @GetMapping("/{id:[0-9]+}/tags")
  public List<TagResponse> getMovieTags (
      @PathVariable @Positive(message = "Movie ID must be positive") Long id) {
    return getMovieTagsUseCase.execute(id);
  }

  @GetMapping("/{id:[0-9]+}/persons")
  public List<MoviePersonResponse> getMoviePersons (
      @PathVariable @Positive(message = "Movie ID must be positive") Long id) {
    return getMoviePersonsUseCase.execute(id);
  }

  @GetMapping("/{id:[0-9]+}/studios")
  public List<MovieStudioResponse> getMovieStudios (
      @PathVariable @Positive(message = "Movie ID must be positive") Long id) {
    return getMovieStudiosUseCase.execute(id);
  }

  @GetMapping("/persons")
  public List<PersonResponse> getPersons () {
    return getPersonsUseCase.execute();
  }

  @GetMapping("/persons/{id:[0-9]+}")
  public PersonResponse getPersonById (
      @PathVariable @Positive(message = "Person ID must be positive") Long id) {
    return getPersonByIdUseCase.execute(id);
  }

  @GetMapping("/studios")
  public List<StudioResponse> getStudios () {
    return getStudiosUseCase.execute();
  }

  @GetMapping("/studios/{id:[0-9]+}")
  public StudioResponse getStudioById (
      @PathVariable @Positive(message = "Studio ID must be positive") Long id) {
    return getStudioByIdUseCase.execute(id);
  }
}