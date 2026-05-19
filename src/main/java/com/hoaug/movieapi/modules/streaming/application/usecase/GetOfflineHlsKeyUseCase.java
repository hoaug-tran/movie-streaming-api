package com.hoaug.movieapi.modules.streaming.application.usecase;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaEpisodeRepository;
import com.hoaug.movieapi.modules.streaming.application.service.HlsPathService;
import com.hoaug.movieapi.modules.streaming.application.service.OfflineTokenService;

@Component
public class GetOfflineHlsKeyUseCase {

  private final JpaEpisodeRepository episodeRepository;
  private final HlsPathService hlsPathService;
  private final OfflineTokenService offlineTokenService;

  public GetOfflineHlsKeyUseCase (JpaEpisodeRepository episodeRepository,
      HlsPathService hlsPathService,
      OfflineTokenService offlineTokenService) {
    this.episodeRepository = episodeRepository;
    this.hlsPathService = hlsPathService;
    this.offlineTokenService = offlineTokenService;
  }

  public byte[] execute (Long episodeId, String quality, String offlineToken) {
    episodeRepository.findById(episodeId)
        .orElseThrow(() -> new AppException(ErrorCode.EPISODE_NOT_FOUND));

    if (!offlineTokenService.isValidOfflineToken(offlineToken, episodeId, quality)) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    try {
      return Files.readAllBytes(hlsPathService.episodeKeyPath(episodeId, quality));
    } catch (IOException e) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }
  }
}
