package com.hoaug.movieapi.modules.streaming.application.service;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.streaming.application.config.MediaStorageProperties;

@Component
public class StreamUrlService {
  private final MediaStorageProperties properties;

  public StreamUrlService(MediaStorageProperties properties) {
    this.properties = properties;
  }

  public String episodePlaylistUrl (Long episodeId) {
    return joinBaseUrl("/stream/series/episodes/" + episodeId + "/master.m3u8");
  }

  public String advertisementPlaylistUrl (Long advertisementId) {
    return joinBaseUrl("/stream/ads/advertisements/" + advertisementId + "/master.m3u8");
  }

  public String episodeKeyUrl (Long episodeId) {
    return joinApiUrl("/stream/keys/series/episodes/" + episodeId);
  }

  public String advertisementKeyUrl (Long advertisementId) {
    return joinApiUrl("/stream/keys/ads/advertisements/" + advertisementId);
  }

  private String joinBaseUrl (String path) {
    String baseUrl = properties.getPublicBaseUrl();
    if (baseUrl.endsWith("/")) {
      return baseUrl.substring(0, baseUrl.length() - 1) + path;
    }
    return baseUrl + path;
  }

  private String joinApiUrl (String path) {
    String baseUrl = properties.getPublicBaseUrl();
    if (baseUrl.endsWith("/")) {
      return baseUrl.substring(0, baseUrl.length() - 1) + "/api/v1" + path;
    }
    return baseUrl + "/api/v1" + path;
  }
}
