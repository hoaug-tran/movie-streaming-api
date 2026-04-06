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
import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMovieRequest;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdateMovieRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.EpisodeResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieDetailResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.CreateEpisodeUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.CreateMovieUseCase;
import com.hoaug.movieapi.modules.movie.application.usecase.DeleteEpisodeUseCase;
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

  public AdminMovieController(CreateMovieUseCase createMovieUseCase,
      UpdateMovieUseCase updateMovieUseCase, DeleteMovieUseCase deleteMovieUseCase,
      CreateEpisodeUseCase createEpisodeUseCase, DeleteEpisodeUseCase deleteEpisodeUseCase) {
    this.createMovieUseCase = createMovieUseCase;
    this.updateMovieUseCase = updateMovieUseCase;
    this.deleteMovieUseCase = deleteMovieUseCase;
    this.createEpisodeUseCase = createEpisodeUseCase;
    this.deleteEpisodeUseCase = deleteEpisodeUseCase;
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
}