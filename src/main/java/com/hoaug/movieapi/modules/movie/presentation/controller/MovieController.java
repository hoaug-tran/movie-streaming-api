package com.hoaug.movieapi.modules.movie.presentation.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.movie.application.dto.request.SearchMovieRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.EpisodeResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailAggregateResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MoviePersonResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieStudioResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieSummaryResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.PersonResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.SearchMovieResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.StudioResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.AdvancedSearchMovieUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetCategoriesCachedUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetEpisodesByMovieUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieByIdUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieBySlugUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieCategoriesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieDetailAggregateUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMoviePersonsUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieStudiosUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieTagsUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMoviesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetPersonByIdUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetPersonsUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetStudioByIdUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetStudiosUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.SearchMovieUseCase;
import com.hoaug.movieapi.modules.searchhistory.application.usecase.CreateSearchHistoryUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("${api.prefix:/api/v1}/movies")
public class MovieController {

  private static final Logger log = LoggerFactory.getLogger(MovieController.class);

  private final GetMoviesUseCase getMoviesUseCase;
  private final GetMovieByIdUseCase getMovieByIdUseCase;
  private final GetMovieBySlugUseCase getMovieBySlugUseCase;
  private final GetEpisodesByMovieUseCase getEpisodesByMovieUseCase;
  private final GetMovieCategoriesUseCase getMovieCategoriesUseCase;
  private final GetCategoriesCachedUseCase getCategoriesCachedUseCase;
  private final GetMovieTagsUseCase getMovieTagsUseCase;
  private final GetMoviePersonsUseCase getMoviePersonsUseCase;
  private final GetMovieStudiosUseCase getMovieStudiosUseCase;
  private final GetPersonsUseCase getPersonsUseCase;
  private final GetPersonByIdUseCase getPersonByIdUseCase;
  private final GetStudiosUseCase getStudiosUseCase;
  private final GetStudioByIdUseCase getStudioByIdUseCase;
  private final SearchMovieUseCase searchMovieUseCase;
  private final AdvancedSearchMovieUseCase advancedSearchMovieUseCase;
  private final GetMovieDetailAggregateUseCase getMovieDetailAggregateUseCase;
  private final MovieRepository movieRepository;
  private final CreateSearchHistoryUseCase createSearchHistoryUseCase;
  private final AuthUserRepository authUserRepository;

  public MovieController(GetMoviesUseCase getMoviesUseCase, GetMovieByIdUseCase getMovieByIdUseCase,
      GetMovieBySlugUseCase getMovieBySlugUseCase,
      GetEpisodesByMovieUseCase getEpisodesByMovieUseCase,
      GetMovieCategoriesUseCase getMovieCategoriesUseCase,
      GetCategoriesCachedUseCase getCategoriesCachedUseCase,
      GetMovieTagsUseCase getMovieTagsUseCase, GetMoviePersonsUseCase getMoviePersonsUseCase,
      GetMovieStudiosUseCase getMovieStudiosUseCase, GetPersonsUseCase getPersonsUseCase,
      GetPersonByIdUseCase getPersonByIdUseCase, GetStudiosUseCase getStudiosUseCase,
      GetStudioByIdUseCase getStudioByIdUseCase, SearchMovieUseCase searchMovieUseCase,
      AdvancedSearchMovieUseCase advancedSearchMovieUseCase,
      GetMovieDetailAggregateUseCase getMovieDetailAggregateUseCase,
      MovieRepository movieRepository,
      CreateSearchHistoryUseCase createSearchHistoryUseCase,
      AuthUserRepository authUserRepository) {
    this.getMoviesUseCase = getMoviesUseCase;
    this.getMovieByIdUseCase = getMovieByIdUseCase;
    this.getMovieBySlugUseCase = getMovieBySlugUseCase;
    this.getEpisodesByMovieUseCase = getEpisodesByMovieUseCase;
    this.getMovieCategoriesUseCase = getMovieCategoriesUseCase;
    this.getCategoriesCachedUseCase = getCategoriesCachedUseCase;
    this.getMovieTagsUseCase = getMovieTagsUseCase;
    this.getMoviePersonsUseCase = getMoviePersonsUseCase;
    this.getMovieStudiosUseCase = getMovieStudiosUseCase;
    this.getPersonsUseCase = getPersonsUseCase;
    this.getPersonByIdUseCase = getPersonByIdUseCase;
    this.getStudiosUseCase = getStudiosUseCase;
    this.getStudioByIdUseCase = getStudioByIdUseCase;
    this.searchMovieUseCase = searchMovieUseCase;
    this.advancedSearchMovieUseCase = advancedSearchMovieUseCase;
    this.getMovieDetailAggregateUseCase = getMovieDetailAggregateUseCase;
    this.movieRepository = movieRepository;
    this.createSearchHistoryUseCase = createSearchHistoryUseCase;
    this.authUserRepository = authUserRepository;
  }

  @GetMapping
  public ResponseEntity<List<MovieSummaryResponse>> getMovies () {
    return ResponseUtil.ok(getMoviesUseCase.execute().getMovies());
  }

  @GetMapping("/categories")
  public ResponseEntity<List<CategoryResponse>> getCategories () {
    return ResponseUtil.ok(getCategoriesCachedUseCase.execute().getCategories());
  }

  @PostMapping("/search")
  public ResponseEntity<SearchMovieResponse> search (@RequestBody SearchMovieRequest request,
      Authentication authentication) {
    SearchMovieResponse response = searchMovieUseCase.execute(request);
    recordSearchKeyword(authentication, request);
    return ResponseUtil.ok(response);
  }

  @PostMapping("/search/advanced")
  public ResponseEntity<SearchMovieResponse> advancedSearch (
      @RequestBody SearchMovieRequest request, Authentication authentication) {
    SearchMovieResponse response = advancedSearchMovieUseCase.execute(request);
    recordSearchKeyword(authentication, request);
    return ResponseUtil.ok(response);
  }

  private void recordSearchKeyword (Authentication authentication, SearchMovieRequest request) {
    if (authentication == null || !authentication.isAuthenticated() || request == null) {
      return;
    }
    String keyword = request.getKeyword();
    if (keyword == null || keyword.trim().isEmpty()) {
      return;
    }
    try {
      String username = authentication.getName();
      if (username == null || username.isBlank() || "anonymousUser".equalsIgnoreCase(username)) {
        return;
      }
      authUserRepository.findByUsername(username).map(User::getId)
          .ifPresent(userId -> createSearchHistoryUseCase.recordKeyword(userId, keyword));
    } catch (Exception ex) {
      log.warn("Failed to persist search history for keyword='{}': {}", keyword, ex.getMessage());
    }
  }

  @GetMapping("/{id:[0-9]+}")
  public ResponseEntity<MovieDetailResponse> getMovieById (
      @PathVariable @Positive(message = "ID phim phải là số dương.") Long id) {
    return ResponseUtil.ok(getMovieByIdUseCase.execute(id));
  }

  @GetMapping("/slug/{slug}")
  public ResponseEntity<MovieDetailResponse> getMovieBySlug (@PathVariable String slug) {
    return ResponseUtil.ok(getMovieBySlugUseCase.execute(slug));
  }

  @GetMapping("/slug/{slug}/detail")
  public ResponseEntity<MovieDetailAggregateResponse> getMovieDetailAggregate (
      @PathVariable String slug) {
    return ResponseUtil.ok(getMovieDetailAggregateUseCase.execute(slug));
  }

  @GetMapping("/{id:[0-9]+}/episodes")
  public ResponseEntity<List<EpisodeResponse>> getEpisodes (
      @PathVariable @Positive(message = "ID phim phải là số dương.") Long id) {
    return ResponseUtil.ok(getEpisodesByMovieUseCase.execute(id));
  }

  @GetMapping("/{id:[0-9]+}/categories")
  public ResponseEntity<List<CategoryResponse>> getMovieCategories (
      @PathVariable @Positive(message = "ID phim phải là số dương.") Long id) {
    return ResponseUtil.ok(getMovieCategoriesUseCase.execute(id));
  }

  @GetMapping("/{id:[0-9]+}/tags")
  public ResponseEntity<List<TagResponse>> getMovieTags (
      @PathVariable @Positive(message = "ID phim phải là số dương.") Long id) {
    return ResponseUtil.ok(getMovieTagsUseCase.execute(id));
  }

  @GetMapping("/{id:[0-9]+}/persons")
  public ResponseEntity<List<MoviePersonResponse>> getMoviePersons (
      @PathVariable @Positive(message = "ID phim phải là số dương.") Long id) {
    return ResponseUtil.ok(getMoviePersonsUseCase.execute(id));
  }

  @GetMapping("/{id:[0-9]+}/studios")
  public ResponseEntity<List<MovieStudioResponse>> getMovieStudios (
      @PathVariable @Positive(message = "ID phim phải là số dương.") Long id) {
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

  @PostMapping("/{id:[0-9]+}/view")
  public ResponseEntity<Void> incrementView (
      @PathVariable @Positive(message = "ID phim phải là số dương.") Long id) {
    movieRepository.incrementViewCount(id);
    return ResponseEntity.noContent().build();
  }
}