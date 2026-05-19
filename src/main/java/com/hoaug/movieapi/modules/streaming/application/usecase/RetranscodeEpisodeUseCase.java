package com.hoaug.movieapi.modules.streaming.application.usecase;

import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.EpisodeEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaEpisodeRepository;
import com.hoaug.movieapi.modules.streaming.application.config.MediaStorageProperties;
import com.hoaug.movieapi.modules.streaming.application.dto.response.MediaUploadResponse;
import com.hoaug.movieapi.modules.streaming.application.service.AsyncTranscodeService;
import com.hoaug.movieapi.modules.streaming.application.service.StreamUrlService;

@Component
public class RetranscodeEpisodeUseCase {

  private static final Logger log = LoggerFactory.getLogger(RetranscodeEpisodeUseCase.class);

  private final JpaEpisodeRepository episodeRepository;
  private final MediaStorageProperties storageProperties;
  private final StreamUrlService streamUrlService;
  private final AsyncTranscodeService asyncTranscodeService;

  public RetranscodeEpisodeUseCase(JpaEpisodeRepository episodeRepository,
      MediaStorageProperties storageProperties, StreamUrlService streamUrlService,
      AsyncTranscodeService asyncTranscodeService) {
    this.episodeRepository = episodeRepository;
    this.storageProperties = storageProperties;
    this.streamUrlService = streamUrlService;
    this.asyncTranscodeService = asyncTranscodeService;
  }

  public MediaUploadResponse execute(Long episodeId) {
    EpisodeEntity episode = episodeRepository.findById(episodeId)
        .orElseThrow(() -> new AppException(ErrorCode.EPISODE_NOT_FOUND));

    Path sourcePath = Path.of(storageProperties.getSeriesDataDirectory())
        .resolve("episodes")
        .resolve(String.valueOf(episodeId))
        .resolve("source.mp4")
        .toAbsolutePath()
        .normalize();

    if (!Files.isRegularFile(sourcePath)) {
      log.error("[Retranscode] Source not found for episode {} at {}", episodeId, sourcePath);
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }

    String mp4Url = streamUrlService.episodeMp4Url(episodeId);
    episode.setVideoUrl(mp4Url);
    episode.setAvailableQualities("TRANSCODING");
    episodeRepository.save(episode);

    log.info("[Retranscode] Re-transcoding episode {} from {}", episodeId, sourcePath);
    asyncTranscodeService.markQueued(episodeId);
    asyncTranscodeService.transcodeEpisodeAsync(episodeId, sourcePath);

    return new MediaUploadResponse(episodeId, mp4Url, "TRANSCODING");
  }
}
