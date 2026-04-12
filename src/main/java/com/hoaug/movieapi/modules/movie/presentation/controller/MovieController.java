package com.hoaug.movieapi.modules.movie.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.movie.application.dto.request.SearchMovieRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.EpisodeResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MoviePersonResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieStudioResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieSummaryResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.PersonResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.SearchMovieResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.StudioResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.AdvancedSearchMovieUseCase;
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
import com.hoaug.movieapi.modules.movie.application.usecase.SearchMovieUseCase;

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
  private final SearchMovieUseCase searchMovieUseCase;
  private final AdvancedSearchMovieUseCase advancedSearchMovieUseCase;

  public MovieController(GetMoviesUseCase getMoviesUseCase, GetMovieByIdUseCase getMovieByIdUseCase,
      GetMovieBySlugUseCase getMovieBySlugUseCase,
      GetEpisodesByMovieUseCase getEpisodesByMovieUseCase,
      GetMovieCategoriesUseCase getMovieCategoriesUseCase, GetMovieTagsUseCase getMovieTagsUseCase,
      GetMoviePersonsUseCase getMoviePersonsUseCase, GetMovieStudiosUseCase getMovieStudiosUseCase,
      GetPersonsUseCase getPersonsUseCase, GetPersonByIdUseCase getPersonByIdUseCase,
      GetStudiosUseCase getStudiosUseCase, GetStudioByIdUseCase getStudioByIdUseCase,
      SearchMovieUseCase searchMovieUseCase,
      AdvancedSearchMovieUseCase advancedSearchMovieUseCase) {
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
    this.searchMovieUseCase = searchMovieUseCase;
    this.advancedSearchMovieUseCase = advancedSearchMovieUseCase;
  }

  @GetMapping
  public ResponseEntity<List<MovieSummaryResponse>> getMovies () {
    return ResponseUtil.ok(getMoviesUseCase.execute());
  }

  @PostMapping("/search")
  public ResponseEntity<SearchMovieResponse> search (@RequestBody SearchMovieRequest request) {
    return ResponseUtil.ok(searchMovieUseCase.execute(request));
  }

  @PostMapping("/search/advanced")
  public ResponseEntity<SearchMovieResponse> advancedSearch (
      @RequestBody SearchMovieRequest request) {
    return ResponseUtil.ok(advancedSearchMovieUseCase.execute(request));
  }

  @GetMapping("/{id:[0-9]+}")
  public ResponseEntity<MovieDetailResponse> getMovieById (
      @PathVariable @Positive(message = "Movie ID must be positive") Long id) {
    return ResponseUtil.ok(getMovieByIdUseCase.execute(id));
  }

  @GetMapping("/slug/{slug}")
  public ResponseEntity<MovieDetailResponse> getMovieBySlug (@PathVariable String slug) {
    return ResponseUtil.ok(getMovieBySlugUseCase.execute(slug));
  }

  @GetMapping("/{id:[0-9]+}/episodes")
  public ResponseEntity<List<EpisodeResponse>> getEpisodes (
      @PathVariable @Positive(message = "Movie ID must be positive") Long id) {
    return ResponseUtil.ok(getEpisodesByMovieUseCase.execute(id));
  }

  @GetMapping("/{id:[0-9]+}/categories")
  public ResponseEntity<List<CategoryResponse>> getMovieCategories (
      @PathVariable @Positive(message = "Movie ID must be positive") Long id) {
    return ResponseUtil.ok(getMovieCategoriesUseCase.execute(id));
  }

  @GetMapping("/{id:[0-9]+}/tags")
  public ResponseEntity<List<TagResponse>> getMovieTags (
      @PathVariable @Positive(message = "Movie ID must be positive") Long id) {
    return ResponseUtil.ok(getMovieTagsUseCase.execute(id));
  }

  @GetMapping("/{id:[0-9]+}/persons")
  public ResponseEntity<List<MoviePersonResponse>> getMoviePersons (
      @PathVariable @Positive(message = "Movie ID must be positive") Long id) {
    return ResponseUtil.ok(getMoviePersonsUseCase.execute(id));
  }

  @GetMapping("/{id:[0-9]+}/studios")
  public ResponseEntity<List<MovieStudioResponse>> getMovieStudios (
      @PathVariable @Positive(message = "Movie ID must be positive") Long id) {
    return ResponseUtil.ok(getMovieStudiosUseCase.execute(id));
  }

  @GetMapping("/persons")
  public ResponseEntity<List<PersonResponse>> getPersons () {
    return ResponseUtil.ok(getPersonsUseCase.execute());
  }

  @GetMapping("/persons/{id:[0-9]+}")
  public ResponseEntity<PersonResponse> getPersonById (
      @PathVariable @Positive(message = "Person ID must be positive") Long id) {
    return ResponseUtil.ok(getPersonByIdUseCase.execute(id));
  }

  @GetMapping("/studios")
  public ResponseEntity<List<StudioResponse>> getStudios () {
    return ResponseUtil.ok(getStudiosUseCase.execute());
  }

  @GetMapping("/studios/{id:[0-9]+}")
  public ResponseEntity<StudioResponse> getStudioById (
      @PathVariable @Positive(message = "Studio ID must be positive") Long id) {
    return ResponseUtil.ok(getStudioByIdUseCase.execute(id));
  }
}