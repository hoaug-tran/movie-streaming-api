package com.hoaug.movieapi.modules.streaming.application.usecase;

import java.nio.file.Path;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.EpisodeEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaEpisodeRepository;
import com.hoaug.movieapi.modules.streaming.application.dto.response.MediaUploadResponse;
import com.hoaug.movieapi.modules.streaming.application.service.AsyncTranscodeService;
import com.hoaug.movieapi.modules.streaming.application.service.Mp4StorageService;
import com.hoaug.movieapi.modules.streaming.application.service.StreamUrlService;

@Component
public class UploadEpisodeSourceUseCase {

  private final JpaEpisodeRepository episodeRepository;
  private final Mp4StorageService storageService;
  private final StreamUrlService streamUrlService;
  private final AsyncTranscodeService asyncTranscodeService;

  public UploadEpisodeSourceUseCase(JpaEpisodeRepository episodeRepository,
      Mp4StorageService storageService, StreamUrlService streamUrlService,
      AsyncTranscodeService asyncTranscodeService) {
    this.episodeRepository = episodeRepository;
    this.storageService = storageService;
    this.streamUrlService = streamUrlService;
    this.asyncTranscodeService = asyncTranscodeService;
  }

  public MediaUploadResponse execute(Long episodeId, MultipartFile file) {
    EpisodeEntity episode = episodeRepository.findById(episodeId)
        .orElseThrow(() -> new AppException(ErrorCode.EPISODE_NOT_FOUND));

    // 1. Store raw source file
    Path sourcePath = storageService.storeEpisodeSource(episodeId, file);

    // 2. Set videoUrl to raw MP4 immediately (available while transcoding)
    String mp4Url = streamUrlService.episodeMp4Url(episodeId);
    episode.setVideoUrl(mp4Url);
    episode.setAvailableQualities("TRANSCODING");
    episodeRepository.save(episode);

    // 3. Kick off async HLS transcode (720p, 1080p, 4K)
    asyncTranscodeService.transcodeEpisodeAsync(episodeId, sourcePath);

    return new MediaUploadResponse(episodeId, mp4Url, "TRANSCODING");
  }
}
