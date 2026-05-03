package com.hoaug.movieapi.modules.streaming.application.usecase;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaEpisodeRepository;
import com.hoaug.movieapi.modules.streaming.application.service.HlsPathService;

@Component
public class GetEpisodeHlsKeyUseCase {
  private final JpaEpisodeRepository episodeRepository;
  private final HlsPathService hlsPathService;

  public GetEpisodeHlsKeyUseCase(JpaEpisodeRepository episodeRepository,
      HlsPathService hlsPathService) {
    this.episodeRepository = episodeRepository;
    this.hlsPathService = hlsPathService;
  }

  public byte[] execute (Long episodeId) {
    episodeRepository.findById(episodeId)
        .orElseThrow(() -> new AppException(ErrorCode.EPISODE_NOT_FOUND));

    try {
      return Files.readAllBytes(hlsPathService.episodeKeyPath(episodeId));
    } catch (IOException exception) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }
  }
}
