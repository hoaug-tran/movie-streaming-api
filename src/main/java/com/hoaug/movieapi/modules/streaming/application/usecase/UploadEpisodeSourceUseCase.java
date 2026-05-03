package com.hoaug.movieapi.modules.streaming.application.usecase;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.EpisodeEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaEpisodeRepository;
import com.hoaug.movieapi.modules.streaming.application.dto.request.HlsTranscodeRequest;
import com.hoaug.movieapi.modules.streaming.application.dto.response.MediaUploadResponse;
import com.hoaug.movieapi.modules.streaming.application.service.HlsPathService;
import com.hoaug.movieapi.modules.streaming.application.service.HlsTranscodeService;
import com.hoaug.movieapi.modules.streaming.application.service.Mp4StorageService;
import com.hoaug.movieapi.modules.streaming.application.service.StreamUrlService;

@Component
public class UploadEpisodeSourceUseCase {
  private final JpaEpisodeRepository episodeRepository;
  private final Mp4StorageService storageService;
  private final StreamUrlService streamUrlService;
  private final HlsPathService hlsPathService;
  private final HlsTranscodeService hlsTranscodeService;

  public UploadEpisodeSourceUseCase(JpaEpisodeRepository episodeRepository,
      Mp4StorageService storageService, StreamUrlService streamUrlService,
      HlsPathService hlsPathService, HlsTranscodeService hlsTranscodeService) {
    this.episodeRepository = episodeRepository;
    this.storageService = storageService;
    this.streamUrlService = streamUrlService;
    this.hlsPathService = hlsPathService;
    this.hlsTranscodeService = hlsTranscodeService;
  }

  public MediaUploadResponse execute (Long episodeId, MultipartFile file) {
    EpisodeEntity episode = episodeRepository.findById(episodeId)
        .orElseThrow(() -> new AppException(ErrorCode.EPISODE_NOT_FOUND));

    var sourcePath = storageService.storeEpisodeSource(episodeId, file);
    String playlistUrl = streamUrlService.episodePlaylistUrl(episodeId);
    hlsTranscodeService.transcode(new HlsTranscodeRequest(
        sourcePath,
        hlsPathService.episodeOutputDirectory(episodeId),
        hlsPathService.episodeKeyPath(episodeId),
        streamUrlService.episodeKeyUrl(episodeId)
    ), playlistUrl);
    episode.setVideoUrl(playlistUrl);
    episodeRepository.save(episode);

    return new MediaUploadResponse(episodeId, playlistUrl, "TRANSCODED");
  }
}
