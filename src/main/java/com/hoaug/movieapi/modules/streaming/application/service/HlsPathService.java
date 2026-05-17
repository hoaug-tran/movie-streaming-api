package com.hoaug.movieapi.modules.streaming.application.service;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.streaming.application.config.MediaStorageProperties;

@Component
public class HlsPathService {
  private final MediaStorageProperties properties;

  public HlsPathService(MediaStorageProperties properties) {
    this.properties = properties;
  }

  public Path episodeOutputDirectory (Long episodeId, String quality) {
    return Path.of(properties.getHlsDirectory(), "series", "episodes", String.valueOf(episodeId), quality)
        .toAbsolutePath()
        .normalize();
  }

  public Path advertisementOutputDirectory (Long advertisementId) {
    return Path.of(properties.getHlsDirectory(), "ads", "advertisements", String.valueOf(advertisementId))
        .toAbsolutePath()
        .normalize();
  }

  public Path episodeKeyPath (Long episodeId, String quality) {
    return Path.of(properties.getKeysDirectory(), "series", "episodes", String.valueOf(episodeId), quality, "video.key")
        .toAbsolutePath()
        .normalize();
  }

  public Path advertisementKeyPath (Long advertisementId) {
    return Path.of(properties.getKeysDirectory(), "ads", "advertisements", String.valueOf(advertisementId), "video.key")
        .toAbsolutePath()
        .normalize();
  }
}
