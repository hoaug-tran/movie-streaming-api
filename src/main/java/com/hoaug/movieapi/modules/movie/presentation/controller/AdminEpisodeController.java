package com.hoaug.movieapi.modules.movie.presentation.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.movie.application.dto.response.AdminEpisodeListItemResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.GetAdminEpisodesUseCase;


@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/episodes")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEpisodeController {

  private final GetAdminEpisodesUseCase getAdminEpisodesUseCase;

  public AdminEpisodeController(GetAdminEpisodesUseCase getAdminEpisodesUseCase) {
    this.getAdminEpisodesUseCase = getAdminEpisodesUseCase;
  }

  @GetMapping
  public List<AdminEpisodeListItemResponse> getEpisodes () {
    return getAdminEpisodesUseCase.execute();
  }
}
