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
import com.hoaug.movieapi.modules.streaming.application.usecase.UploadImageUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.UploadMovieSourceUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.UploadVideoUseCase;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/media")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMediaController {

  private final UploadEpisodeSourceUseCase uploadEpisodeSourceUseCase;
  private final UploadAdvertisementSourceUseCase uploadAdvertisementSourceUseCase;
  private final UploadMovieSourceUseCase uploadMovieSourceUseCase;
  private final UploadImageUseCase uploadImageUseCase;
  private final UploadVideoUseCase uploadVideoUseCase;

  public AdminMediaController (UploadEpisodeSourceUseCase uploadEpisodeSourceUseCase,
      UploadAdvertisementSourceUseCase uploadAdvertisementSourceUseCase,
      UploadMovieSourceUseCase uploadMovieSourceUseCase,
      UploadImageUseCase uploadImageUseCase,
      UploadVideoUseCase uploadVideoUseCase) {
    this.uploadEpisodeSourceUseCase = uploadEpisodeSourceUseCase;
    this.uploadAdvertisementSourceUseCase = uploadAdvertisementSourceUseCase;
    this.uploadMovieSourceUseCase = uploadMovieSourceUseCase;
    this.uploadImageUseCase = uploadImageUseCase;
    this.uploadVideoUseCase = uploadVideoUseCase;
  }

  @PostMapping("/episodes/{episodeId}/source")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public MediaUploadResponse uploadEpisodeSource (@PathVariable Long episodeId,
      @RequestPart("file") MultipartFile file) {
    return uploadEpisodeSourceUseCase.execute(episodeId, file);
  }

  @PostMapping("/movies/{movieId}/source")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public MediaUploadResponse uploadMovieSource (@PathVariable Long movieId,
      @RequestPart("file") MultipartFile file) {
    return uploadMovieSourceUseCase.execute(movieId, file);
  }

  @PostMapping("/advertisements/{advertisementId}/source")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public MediaUploadResponse uploadAdvertisementSource (@PathVariable Long advertisementId,
      @RequestPart("file") MultipartFile file) {
    return uploadAdvertisementSourceUseCase.execute(advertisementId, file);
  }

  @PostMapping("/images")
  @ResponseStatus(HttpStatus.CREATED)
  public MediaUploadResponse uploadImage (@RequestPart("file") MultipartFile file) {
    return uploadImageUseCase.execute(file);
  }

  @PostMapping("/videos")
  @ResponseStatus(HttpStatus.CREATED)
  public MediaUploadResponse uploadVideo (@RequestPart("file") MultipartFile file) {
    return uploadVideoUseCase.execute(file);
  }
}
