package com.hoaug.movieapi.modules.streaming.application.usecase;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
import com.hoaug.movieapi.modules.streaming.application.service.Mp4StorageService;
import com.hoaug.movieapi.modules.streaming.application.service.StreamUrlService;

@Component
public class RetranscodeMovieUseCase {

  private static final Logger log = LoggerFactory.getLogger(RetranscodeMovieUseCase.class);

  private final JpaEpisodeRepository episodeRepository;
  private final StreamUrlService streamUrlService;
  private final AsyncTranscodeService asyncTranscodeService;
  private final Mp4StorageService storageService;

  public RetranscodeMovieUseCase(JpaEpisodeRepository episodeRepository,
      MediaStorageProperties storageProperties, StreamUrlService streamUrlService,
      AsyncTranscodeService asyncTranscodeService, Mp4StorageService storageService) {
    this.episodeRepository = episodeRepository;
    this.streamUrlService = streamUrlService;
    this.asyncTranscodeService = asyncTranscodeService;
    this.storageService = storageService;
  }

  public List<MediaUploadResponse> execute(Long movieId) {
    List<EpisodeEntity> episodes = episodeRepository.findByMovieIdOrderByEpisodeNumberAsc(movieId);
    if (episodes.isEmpty()) {
      throw new AppException(ErrorCode.EPISODE_NOT_FOUND);
    }

    List<MediaUploadResponse> results = new ArrayList<>();
    int queued = 0;
    int skipped = 0;

    for (EpisodeEntity episode : episodes) {
      Long episodeId = episode.getId();
      Path sourcePath = storageService.findEpisodeSource(episodeId).orElse(null);

      if (sourcePath == null) {
        log.warn("[Retranscode-Movie] Skip episode {} (no source file)", episodeId);
        skipped++;
        continue;
      }

      String mp4Url = streamUrlService.episodeMp4Url(episodeId);
      episode.setVideoUrl(mp4Url);
      episode.setAvailableQualities("TRANSCODING");
      episodeRepository.save(episode);

      asyncTranscodeService.markQueued(episodeId);
      asyncTranscodeService.transcodeEpisodeAsync(episodeId, sourcePath);
      results.add(new MediaUploadResponse(episodeId, mp4Url, "TRANSCODING"));
      queued++;
    }

    log.info("[Retranscode-Movie] movieId={} queued={} skipped={}", movieId, queued, skipped);
    return results;
  }
}
