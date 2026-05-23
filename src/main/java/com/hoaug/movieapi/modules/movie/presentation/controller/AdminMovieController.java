package com.hoaug.movieapi.modules.movie.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateEpisodeRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieCategoryRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMoviePersonRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieStudioRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieTagRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreatePersonRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateStudioRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateEpisodeRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateMovieInteractionLocksRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateMovieRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateMovieStatusRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.EpisodeResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MoviePersonResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieStudioResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieSummaryResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.PersonResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.StudioResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.TagResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.CreateEpisodeUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.CreateMovieCategoryUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.CreateMoviePersonUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.CreateMovieStudioUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.CreateMovieTagUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.CreateMovieUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.CreatePersonUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.CreateStudioUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.DeleteEpisodeUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.DeleteMovieCategoryUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.DeleteMoviePersonUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.DeleteMovieStudioUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.DeleteMovieTagUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.DeleteMovieUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetAdminMoviesUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.GetMovieByIdAdminUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.UpdateEpisodeUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.UpdateMovieInteractionLocksUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.UpdateMoviePersonUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.UpdateMovieStatusUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.UpdateMovieStudioUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.UpdateMovieUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/movies")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMovieController {

  private final GetAdminMoviesUseCase getAdminMoviesUseCase;
  private final GetMovieByIdAdminUseCase getMovieByIdAdminUseCase;
  private final CreateMovieUseCase createMovieUseCase;
  private final UpdateMovieUseCase updateMovieUseCase;
  private final UpdateMovieStatusUseCase updateMovieStatusUseCase;
  private final UpdateMovieInteractionLocksUseCase updateMovieInteractionLocksUseCase;
  private final DeleteMovieUseCase deleteMovieUseCase;
  private final UpdateEpisodeUseCase updateEpisodeUseCase;
  private final CreateEpisodeUseCase createEpisodeUseCase;
  private final DeleteEpisodeUseCase deleteEpisodeUseCase;
  private final CreatePersonUseCase createPersonUseCase;
  private final CreateStudioUseCase createStudioUseCase;
  private final CreateMovieCategoryUseCase createMovieCategoryUseCase;
  private final DeleteMovieCategoryUseCase deleteMovieCategoryUseCase;
  private final CreateMovieTagUseCase createMovieTagUseCase;
  private final DeleteMovieTagUseCase deleteMovieTagUseCase;
  private final CreateMoviePersonUseCase createMoviePersonUseCase;
  private final UpdateMoviePersonUseCase updateMoviePersonUseCase;
  private final DeleteMoviePersonUseCase deleteMoviePersonUseCase;
  private final CreateMovieStudioUseCase createMovieStudioUseCase;
  private final UpdateMovieStudioUseCase updateMovieStudioUseCase;
  private final DeleteMovieStudioUseCase deleteMovieStudioUseCase;

  public AdminMovieController(GetAdminMoviesUseCase getAdminMoviesUseCase,
      GetMovieByIdAdminUseCase getMovieByIdAdminUseCase, CreateMovieUseCase createMovieUseCase,
      UpdateMovieUseCase updateMovieUseCase, UpdateMovieStatusUseCase updateMovieStatusUseCase,
      UpdateMovieInteractionLocksUseCase updateMovieInteractionLocksUseCase,
      DeleteMovieUseCase deleteMovieUseCase, UpdateEpisodeUseCase updateEpisodeUseCase,
      CreateEpisodeUseCase createEpisodeUseCase, DeleteEpisodeUseCase deleteEpisodeUseCase,
      CreatePersonUseCase createPersonUseCase, CreateStudioUseCase createStudioUseCase,
      CreateMovieCategoryUseCase createMovieCategoryUseCase,
      DeleteMovieCategoryUseCase deleteMovieCategoryUseCase,
      CreateMovieTagUseCase createMovieTagUseCase, DeleteMovieTagUseCase deleteMovieTagUseCase,
      CreateMoviePersonUseCase createMoviePersonUseCase,
      UpdateMoviePersonUseCase updateMoviePersonUseCase,
      DeleteMoviePersonUseCase deleteMoviePersonUseCase,
      CreateMovieStudioUseCase createMovieStudioUseCase,
      UpdateMovieStudioUseCase updateMovieStudioUseCase,
      DeleteMovieStudioUseCase deleteMovieStudioUseCase) {
    this.getAdminMoviesUseCase = getAdminMoviesUseCase;
    this.getMovieByIdAdminUseCase = getMovieByIdAdminUseCase;
    this.createMovieUseCase = createMovieUseCase;
    this.updateMovieUseCase = updateMovieUseCase;
    this.updateMovieStatusUseCase = updateMovieStatusUseCase;
    this.updateMovieInteractionLocksUseCase = updateMovieInteractionLocksUseCase;
    this.deleteMovieUseCase = deleteMovieUseCase;
    this.updateEpisodeUseCase = updateEpisodeUseCase;
    this.createEpisodeUseCase = createEpisodeUseCase;
    this.deleteEpisodeUseCase = deleteEpisodeUseCase;
    this.createPersonUseCase = createPersonUseCase;
    this.createStudioUseCase = createStudioUseCase;
    this.createMovieCategoryUseCase = createMovieCategoryUseCase;
    this.deleteMovieCategoryUseCase = deleteMovieCategoryUseCase;
    this.createMovieTagUseCase = createMovieTagUseCase;
    this.deleteMovieTagUseCase = deleteMovieTagUseCase;
    this.createMoviePersonUseCase = createMoviePersonUseCase;
    this.updateMoviePersonUseCase = updateMoviePersonUseCase;
    this.deleteMoviePersonUseCase = deleteMoviePersonUseCase;
    this.createMovieStudioUseCase = createMovieStudioUseCase;
    this.updateMovieStudioUseCase = updateMovieStudioUseCase;
    this.deleteMovieStudioUseCase = deleteMovieStudioUseCase;
  }

  @GetMapping
  public ResponseEntity<List<MovieSummaryResponse>> getMovies () {
    return ResponseUtil.ok(getAdminMoviesUseCase.execute().getMovies());
  }

  @GetMapping("/{id}")
  public MovieDetailResponse getMovie (@PathVariable Long id) {
    return getMovieByIdAdminUseCase.execute(id);
  }

  @PostMapping
  public MovieDetailResponse createMovie (@Valid @RequestBody CreateMovieRequest request) {
    return createMovieUseCase.execute(request);
  }

  @PutMapping("/{id}")
  public MovieDetailResponse updateMovie (@PathVariable Long id,
      @Valid @RequestBody UpdateMovieRequest request) {
    return updateMovieUseCase.execute(id, request);
  }

  @PatchMapping("/{id}/status")
  public MovieDetailResponse updateMovieStatus (@PathVariable Long id,
      @Valid @RequestBody UpdateMovieStatusRequest request) {
    return updateMovieStatusUseCase.execute(id, request);
  }

  @PatchMapping("/{id}/interaction-locks")
  @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
  public MovieDetailResponse updateMovieInteractionLocks (@PathVariable Long id,
      @Valid @RequestBody UpdateMovieInteractionLocksRequest request) {
    return updateMovieInteractionLocksUseCase.execute(id, request);
  }

  @DeleteMapping("/{id}")
  public void deleteMovie (@PathVariable Long id) {
    deleteMovieUseCase.execute(id);
  }

  @DeleteMapping("/{id}/episodes/{episodeId}")
  public void deleteEpisode (@PathVariable Long id, @PathVariable Long episodeId) {
    deleteEpisodeUseCase.execute(id, episodeId);
  }

  @PutMapping("/{id}/episodes/{episodeId}")
  public EpisodeResponse updateEpisode (@PathVariable Long id, @PathVariable Long episodeId,
      @Valid @RequestBody UpdateEpisodeRequest request) {
    return updateEpisodeUseCase.execute(id, episodeId, request);
  }

  @PostMapping("/{id}/episodes")
  public EpisodeResponse createEpisode (@PathVariable Long id,
      @Valid @RequestBody CreateEpisodeRequest request) {
    return createEpisodeUseCase.execute(id, request);
  }

  @PostMapping("/persons")
  public PersonResponse createPerson (@Valid @RequestBody CreatePersonRequest request) {
    return createPersonUseCase.execute(request);
  }

  @PostMapping("/studios")
  public StudioResponse createStudio (@Valid @RequestBody CreateStudioRequest request) {
    return createStudioUseCase.execute(request);
  }

  @PostMapping("/{id}/categories")
  public CategoryResponse addMovieCategory (@PathVariable Long id,
      @Valid @RequestBody CreateMovieCategoryRequest request) {
    return createMovieCategoryUseCase.execute(id, request);
  }

  @DeleteMapping("/{id}/categories/{categoryId}")
  public void removeMovieCategory (@PathVariable Long id, @PathVariable Long categoryId) {
    deleteMovieCategoryUseCase.execute(id, categoryId);
  }

  @PostMapping("/{id}/tags")
  public TagResponse addMovieTag (@PathVariable Long id,
      @Valid @RequestBody CreateMovieTagRequest request) {
    return createMovieTagUseCase.execute(id, request);
  }

  @DeleteMapping("/{id}/tags/{tagId}")
  public void removeMovieTag (@PathVariable Long id, @PathVariable Long tagId) {
    deleteMovieTagUseCase.execute(id, tagId);
  }

  @PostMapping("/{id}/persons")
  public MoviePersonResponse addMoviePerson (@PathVariable Long id,
      @Valid @RequestBody CreateMoviePersonRequest request) {
    return createMoviePersonUseCase.execute(id, request);
  }

  @DeleteMapping("/movie-persons/{id}")
  public void removeMoviePerson (@PathVariable Long id) {
    deleteMoviePersonUseCase.execute(id);
  }

  @PutMapping("/movie-persons/{id}")
  public MoviePersonResponse updateMoviePerson (@PathVariable Long id,
      @Valid @RequestBody CreateMoviePersonRequest request) {
    return updateMoviePersonUseCase.execute(id, request);
  }

  @PostMapping("/{id}/studios")
  public MovieStudioResponse addMovieStudio (@PathVariable Long id,
      @Valid @RequestBody CreateMovieStudioRequest request) {
    return createMovieStudioUseCase.execute(id, request);
  }

  @DeleteMapping("/movie-studios/{id}")
  public void removeMovieStudio (@PathVariable Long id) {
    deleteMovieStudioUseCase.execute(id);
  }

  @PutMapping("/movie-studios/{id}")
  public MovieStudioResponse updateMovieStudio (@PathVariable Long id,
      @Valid @RequestBody CreateMovieStudioRequest request) {
    return updateMovieStudioUseCase.execute(id, request);
  }
}