package com.hoaug.movieapi.modules.streaming.presentation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.modules.streaming.application.dto.response.MediaUploadResponse;
import com.hoaug.movieapi.modules.streaming.application.dto.response.TranscodeProgressResponse;
import com.hoaug.movieapi.modules.streaming.application.service.TranscodeProgressTracker;
import com.hoaug.movieapi.modules.streaming.application.usecase.RetranscodeEpisodeUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.RetranscodeMovieUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.UploadAdvertisementSourceUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.UploadEpisodeSourceUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.UploadImageUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.UploadMovieSourceUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.UploadMovieTrailerUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.UploadSeriesTrailerUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.UploadVideoUseCase;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/media")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMediaController {

  private final UploadEpisodeSourceUseCase uploadEpisodeSourceUseCase;
  private final UploadAdvertisementSourceUseCase uploadAdvertisementSourceUseCase;
  private final UploadMovieSourceUseCase uploadMovieSourceUseCase;
  private final UploadMovieTrailerUseCase uploadMovieTrailerUseCase;
  private final UploadSeriesTrailerUseCase uploadSeriesTrailerUseCase;
  private final UploadImageUseCase uploadImageUseCase;
  private final UploadVideoUseCase uploadVideoUseCase;
  private final RetranscodeEpisodeUseCase retranscodeEpisodeUseCase;
  private final RetranscodeMovieUseCase retranscodeMovieUseCase;
  private final TranscodeProgressTracker progressTracker;

  public AdminMediaController (UploadEpisodeSourceUseCase uploadEpisodeSourceUseCase,
      UploadAdvertisementSourceUseCase uploadAdvertisementSourceUseCase,
      UploadMovieSourceUseCase uploadMovieSourceUseCase,
      UploadMovieTrailerUseCase uploadMovieTrailerUseCase,
      UploadSeriesTrailerUseCase uploadSeriesTrailerUseCase,
      UploadImageUseCase uploadImageUseCase,
      UploadVideoUseCase uploadVideoUseCase,
      RetranscodeEpisodeUseCase retranscodeEpisodeUseCase,
      RetranscodeMovieUseCase retranscodeMovieUseCase,
      TranscodeProgressTracker progressTracker) {
    this.uploadEpisodeSourceUseCase = uploadEpisodeSourceUseCase;
    this.uploadAdvertisementSourceUseCase = uploadAdvertisementSourceUseCase;
    this.uploadMovieSourceUseCase = uploadMovieSourceUseCase;
    this.uploadMovieTrailerUseCase = uploadMovieTrailerUseCase;
    this.uploadSeriesTrailerUseCase = uploadSeriesTrailerUseCase;
    this.uploadImageUseCase = uploadImageUseCase;
    this.uploadVideoUseCase = uploadVideoUseCase;
    this.retranscodeEpisodeUseCase = retranscodeEpisodeUseCase;
    this.retranscodeMovieUseCase = retranscodeMovieUseCase;
    this.progressTracker = progressTracker;
  }

  @PostMapping("/episodes/{episodeId}/source")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public MediaUploadResponse uploadEpisodeSource (@PathVariable Long episodeId,
      @RequestPart("file") MultipartFile file) {
    return uploadEpisodeSourceUseCase.execute(episodeId, file);
  }

  @PostMapping("/episodes/{episodeId}/retranscode")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public MediaUploadResponse retranscodeEpisode (@PathVariable Long episodeId) {
    return retranscodeEpisodeUseCase.execute(episodeId);
  }

  @PostMapping("/episodes/{episodeId}/trailer")
  @ResponseStatus(HttpStatus.CREATED)
  public MediaUploadResponse uploadSeriesTrailer (@PathVariable Long episodeId,
      @RequestPart("file") MultipartFile file) {
    return uploadSeriesTrailerUseCase.execute(episodeId, file);
  }

  @PostMapping("/movies/{movieId}/source")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public MediaUploadResponse uploadMovieSource (@PathVariable Long movieId,
      @RequestPart("file") MultipartFile file) {
    return uploadMovieSourceUseCase.execute(movieId, file);
  }

  @PostMapping("/movies/{movieId}/trailer")
  @ResponseStatus(HttpStatus.CREATED)
  public MediaUploadResponse uploadMovieTrailer (@PathVariable Long movieId,
      @RequestPart("file") MultipartFile file) {
    return uploadMovieTrailerUseCase.execute(movieId, file);
  }
   @PostMapping("/movies/trailer")
   @ResponseStatus(HttpStatus.CREATED)
   public MediaUploadResponse uploadMovieTrailerTemp (
       @RequestPart("file") MultipartFile file) {
     return uploadMovieTrailerUseCase.executeTemp(file);
   }

   @PostMapping("/movies/{movieId}/retranscode")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public List<MediaUploadResponse> retranscodeMovie (@PathVariable Long movieId) {
    return retranscodeMovieUseCase.execute(movieId);
  }

  @GetMapping("/episodes/{episodeId}/transcode-progress")
  public ResponseEntity<TranscodeProgressResponse> transcodeProgress (@PathVariable Long episodeId) {
    TranscodeProgressTracker.EpisodeProgress p = progressTracker.get(episodeId);
    if (p == null) return ResponseEntity.notFound().build();
    TranscodeProgressResponse r = new TranscodeProgressResponse();
    r.episodeId = p.episodeId;
    r.status = p.status.name();
    r.targetQualities = p.targetQualities;
    r.completedQualities = p.completedQualities;
    r.failedQualities = p.failedQualities;
    r.skippedQualities = p.skippedQualities;
    r.currentQuality = p.currentQuality;
    r.message = p.message;
    r.startedAt = p.startedAt;
    r.updatedAt = p.updatedAt;
    r.finishedAt = p.finishedAt;
    int total = Math.max(1, p.targetQualities.size());
    int done = p.completedQualities.size() + p.skippedQualities.size() + p.failedQualities.size();
    r.percent = Math.min(100, Math.round((done * 100f) / total));
    return ResponseEntity.ok(r);
  }

  @GetMapping("/transcode-progress")
  public Map<Long, TranscodeProgressResponse> transcodeProgressBatch (
      @RequestParam("episodeIds") List<Long> episodeIds) {
    java.util.LinkedHashMap<Long, TranscodeProgressResponse> result = new java.util.LinkedHashMap<>();
    for (Long id : episodeIds) {
      TranscodeProgressTracker.EpisodeProgress p = progressTracker.get(id);
      if (p == null) continue;
      TranscodeProgressResponse r = new TranscodeProgressResponse();
      r.episodeId = p.episodeId;
      r.status = p.status.name();
      r.targetQualities = p.targetQualities;
      r.completedQualities = p.completedQualities;
      r.failedQualities = p.failedQualities;
      r.skippedQualities = p.skippedQualities;
      r.currentQuality = p.currentQuality;
      r.message = p.message;
      r.startedAt = p.startedAt;
      r.updatedAt = p.updatedAt;
      r.finishedAt = p.finishedAt;
      int total = Math.max(1, p.targetQualities.size());
      int done = p.completedQualities.size() + p.skippedQualities.size() + p.failedQualities.size();
      r.percent = Math.min(100, Math.round((done * 100f) / total));
      result.put(id, r);
    }
    return result;
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




