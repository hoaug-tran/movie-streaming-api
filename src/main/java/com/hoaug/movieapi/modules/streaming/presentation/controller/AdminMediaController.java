package com.hoaug.movieapi.modules.streaming.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.modules.streaming.application.dto.response.MediaUploadResponse;
import com.hoaug.movieapi.modules.streaming.application.usecase.UploadAdvertisementSourceUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.UploadEpisodeSourceUseCase;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/media")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMediaController {
  private final UploadEpisodeSourceUseCase uploadEpisodeSourceUseCase;
  private final UploadAdvertisementSourceUseCase uploadAdvertisementSourceUseCase;

  public AdminMediaController(UploadEpisodeSourceUseCase uploadEpisodeSourceUseCase,
      UploadAdvertisementSourceUseCase uploadAdvertisementSourceUseCase) {
    this.uploadEpisodeSourceUseCase = uploadEpisodeSourceUseCase;
    this.uploadAdvertisementSourceUseCase = uploadAdvertisementSourceUseCase;
  }

  @PostMapping("/episodes/{episodeId}/source")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public MediaUploadResponse uploadEpisodeSource (@PathVariable Long episodeId,
      @RequestPart("file") MultipartFile file) {
    return uploadEpisodeSourceUseCase.execute(episodeId, file);
  }

  @PostMapping("/advertisements/{advertisementId}/source")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public MediaUploadResponse uploadAdvertisementSource (@PathVariable Long advertisementId,
      @RequestPart("file") MultipartFile file) {
    return uploadAdvertisementSourceUseCase.execute(advertisementId, file);
  }
}
