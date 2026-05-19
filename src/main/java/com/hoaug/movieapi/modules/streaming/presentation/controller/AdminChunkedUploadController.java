package com.hoaug.movieapi.modules.streaming.presentation.controller;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.EpisodeEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaEpisodeRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieRepository;
import com.hoaug.movieapi.modules.streaming.application.dto.response.MediaUploadResponse;
import com.hoaug.movieapi.modules.streaming.application.service.AsyncTranscodeService;
import com.hoaug.movieapi.modules.streaming.application.service.ChunkedUploadService;
import com.hoaug.movieapi.modules.streaming.application.service.Mp4StorageService;
import com.hoaug.movieapi.modules.streaming.application.service.StreamUrlService;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/media/chunked")
@PreAuthorize("hasRole('ADMIN')")
public class AdminChunkedUploadController {

  private final ChunkedUploadService chunkedUploadService;
  private final Mp4StorageService storageService;
  private final StreamUrlService streamUrlService;
  private final AsyncTranscodeService asyncTranscodeService;
  private final JpaEpisodeRepository episodeRepository;
  private final JpaMovieRepository movieRepository;

  public AdminChunkedUploadController(ChunkedUploadService chunkedUploadService,
      Mp4StorageService storageService, StreamUrlService streamUrlService,
      AsyncTranscodeService asyncTranscodeService, JpaEpisodeRepository episodeRepository,
      JpaMovieRepository movieRepository) {
    this.chunkedUploadService = chunkedUploadService;
    this.storageService = storageService;
    this.streamUrlService = streamUrlService;
    this.asyncTranscodeService = asyncTranscodeService;
    this.episodeRepository = episodeRepository;
    this.movieRepository = movieRepository;
  }

  @PostMapping("/init")
  @ResponseStatus(HttpStatus.CREATED)
  public Map<String, Object> init (@RequestParam("fileName") String fileName,
      @RequestParam("fileSize") long fileSize, @RequestParam("totalChunks") int totalChunks) {
    return chunkedUploadService.init(fileName, fileSize, totalChunks);
  }

  @PostMapping("/{uploadId}/chunks/{index}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Map<String, Object> appendChunk (@PathVariable String uploadId,
      @PathVariable("index") int chunkIndex, @RequestPart("file") MultipartFile file) {
    chunkedUploadService.appendChunk(uploadId, chunkIndex, file);
    Map<String, Object> resp = new HashMap<>();
    resp.put("uploadId", uploadId);
    resp.put("chunkIndex", chunkIndex);
    return resp;
  }

  @PostMapping("/{uploadId}/finalize/episode/{episodeId}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public MediaUploadResponse finalizeEpisode (@PathVariable String uploadId,
      @PathVariable Long episodeId) {
    EpisodeEntity episode = episodeRepository.findById(episodeId)
        .orElseThrow( () -> new AppException(ErrorCode.EPISODE_NOT_FOUND));

    String fileName = chunkedUploadService.getFileName(uploadId);
    Path destination = storageService.resolveEpisodeDestination(episodeId, fileName);
    Path saved = chunkedUploadService.assemble(uploadId, destination);

    String mp4Url = streamUrlService.episodeMp4Url(episodeId);
    episode.setVideoUrl(mp4Url);
    episode.setAvailableQualities("TRANSCODING");
    episodeRepository.save(episode);

    asyncTranscodeService.transcodeEpisodeAsync(episodeId, saved);
    return new MediaUploadResponse(episodeId, mp4Url, "TRANSCODING");
  }

  @PostMapping("/{uploadId}/finalize/movie/{movieId}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public MediaUploadResponse finalizeMovie (@PathVariable String uploadId,
      @PathVariable Long movieId) {
    MovieEntity movie = movieRepository.findById(movieId)
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    String fileName = chunkedUploadService.getFileName(uploadId);
    Path destination = storageService.resolveMovieDestination(movieId, fileName);
    chunkedUploadService.assemble(uploadId, destination);

    String videoUrl = streamUrlService.movieMp4Url(movieId);
    movie.setTrailerUrl(videoUrl);
    movieRepository.save(movie);

    return new MediaUploadResponse(movieId, videoUrl, "UPLOADED");
  }

  @DeleteMapping("/{uploadId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void abort (@PathVariable String uploadId) {
    chunkedUploadService.abort(uploadId);
  }
}
