package com.hoaug.movieapi.modules.movie.presentation.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.movie.application.dto.request.CreateEpisodeRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieCategoryRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMoviePersonRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieStudioRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieTagRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreatePersonRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateStudioRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateMovieRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.CategoryResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.EpisodeResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MoviePersonResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieStudioResponse;
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
import com.hoaug.movieapi.modules.movie.application.usecase.UpdateMovieUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/movies")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMovieController {

  private final CreateMovieUseCase createMovieUseCase;
  private final UpdateMovieUseCase updateMovieUseCase;
  private final DeleteMovieUseCase deleteMovieUseCase;
  private final CreateEpisodeUseCase createEpisodeUseCase;
  private final DeleteEpisodeUseCase deleteEpisodeUseCase;
  private final CreatePersonUseCase createPersonUseCase;
  private final CreateStudioUseCase createStudioUseCase;
  private final CreateMovieCategoryUseCase createMovieCategoryUseCase;
  private final DeleteMovieCategoryUseCase deleteMovieCategoryUseCase;
  private final CreateMovieTagUseCase createMovieTagUseCase;
  private final DeleteMovieTagUseCase deleteMovieTagUseCase;
  private final CreateMoviePersonUseCase createMoviePersonUseCase;
  private final DeleteMoviePersonUseCase deleteMoviePersonUseCase;
  private final CreateMovieStudioUseCase createMovieStudioUseCase;
  private final DeleteMovieStudioUseCase deleteMovieStudioUseCase;

  public AdminMovieController(CreateMovieUseCase createMovieUseCase,
      UpdateMovieUseCase updateMovieUseCase, DeleteMovieUseCase deleteMovieUseCase,
      CreateEpisodeUseCase createEpisodeUseCase, DeleteEpisodeUseCase deleteEpisodeUseCase,
      CreatePersonUseCase createPersonUseCase, CreateStudioUseCase createStudioUseCase,
      CreateMovieCategoryUseCase createMovieCategoryUseCase,
      DeleteMovieCategoryUseCase deleteMovieCategoryUseCase,
      CreateMovieTagUseCase createMovieTagUseCase, DeleteMovieTagUseCase deleteMovieTagUseCase,
      CreateMoviePersonUseCase createMoviePersonUseCase,
      DeleteMoviePersonUseCase deleteMoviePersonUseCase,
      CreateMovieStudioUseCase createMovieStudioUseCase,
      DeleteMovieStudioUseCase deleteMovieStudioUseCase) {
    this.createMovieUseCase = createMovieUseCase;
    this.updateMovieUseCase = updateMovieUseCase;
    this.deleteMovieUseCase = deleteMovieUseCase;
    this.createEpisodeUseCase = createEpisodeUseCase;
    this.deleteEpisodeUseCase = deleteEpisodeUseCase;
    this.createPersonUseCase = createPersonUseCase;
    this.createStudioUseCase = createStudioUseCase;
    this.createMovieCategoryUseCase = createMovieCategoryUseCase;
    this.deleteMovieCategoryUseCase = deleteMovieCategoryUseCase;
    this.createMovieTagUseCase = createMovieTagUseCase;
    this.deleteMovieTagUseCase = deleteMovieTagUseCase;
    this.createMoviePersonUseCase = createMoviePersonUseCase;
    this.deleteMoviePersonUseCase = deleteMoviePersonUseCase;
    this.createMovieStudioUseCase = createMovieStudioUseCase;
    this.deleteMovieStudioUseCase = deleteMovieStudioUseCase;
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

  @DeleteMapping("/{id}")
  public void deleteMovie (@PathVariable Long id) {
    deleteMovieUseCase.execute(id);
  }

  @DeleteMapping("/{id}/episodes/{episodeId}")
  public void deleteEpisode (@PathVariable Long id, @PathVariable Long episodeId) {
    deleteEpisodeUseCase.execute(id, episodeId);
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

  @PostMapping("/{id}/studios")
  public MovieStudioResponse addMovieStudio (@PathVariable Long id,
      @Valid @RequestBody CreateMovieStudioRequest request) {
    return createMovieStudioUseCase.execute(id, request);
  }

  @DeleteMapping("/movie-studios/{id}")
  public void removeMovieStudio (@PathVariable Long id) {
    deleteMovieStudioUseCase.execute(id);
  }
}