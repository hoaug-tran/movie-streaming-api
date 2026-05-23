package com.hoaug.movieapi.modules.streaming.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.streaming.application.config.MediaStorageProperties;

@Component
public class StreamUrlService {
  private final MediaStorageProperties properties;
  private final String apiPrefix;

  public StreamUrlService(MediaStorageProperties properties,
      @Value("${api.prefix:/api/v1}") String apiPrefix) {
    this.properties = properties;
    this.apiPrefix = normalizePrefix(apiPrefix);
  }

  public String episodePlaylistUrl (Long episodeId, String quality) {
    return joinBaseUrl("/stream/series/episodes/" + episodeId + "/" + quality + "/master.m3u8");
  }

  public String episodeMp4Url (Long episodeId) {
    return joinBaseUrl("/data/series/episodes/" + episodeId + "/source.mp4");
  }

  public String movieMp4Url (Long movieId) {
    return joinBaseUrl("/data/movies/" + movieId + "/source.mp4");
  }

  public String imageUrl (String filename) {
    return joinBaseUrl("/images/" + filename);
  }

  public String othersVideoUrl (String filename) {
    return joinBaseUrl("/data/others/" + filename);
  }

  public String advertisementPlaylistUrl (Long advertisementId) {
    return joinBaseUrl("/stream/ads/advertisements/" + advertisementId + "/master.m3u8");
  }

  public String episodeKeyUrl (Long episodeId, String quality) {
    return joinApiUrl("/stream/keys/series/episodes/" + episodeId + "/" + quality);
  }

  public String offlineEpisodeKeyUrl (Long episodeId, String quality) {
    return joinApiUrl("/stream/offline/key/" + episodeId + "/" + quality);
  }

  public String episodeSegmentUrl (Long episodeId, String quality, String segmentFilename) {
    return joinBaseUrl(
        "/stream/series/episodes/" + episodeId + "/" + quality + "/" + segmentFilename);
  }

  public String advertisementKeyUrl (Long advertisementId) {
    return joinApiUrl("/stream/keys/ads/advertisements/" + advertisementId);
  }

  private String joinBaseUrl (String path) {
    return trimTrailingSlash(properties.getPublicBaseUrl()) + path;
  }

  private String joinApiUrl (String path) {
    return trimTrailingSlash(properties.getPublicBaseUrl()) + apiPrefix + path;
  }

  private String trimTrailingSlash (String value) {
    if (value == null || value.isBlank()) {
      return "";
    }

    if (value.endsWith("/")) {
      return value.substring(0, value.length() - 1);
    }

    return value;
  }

  private String normalizePrefix (String value) {
    if (value == null || value.isBlank()) {
      return "";
    }

    String prefix = value.startsWith("/") ? value : "/" + value;

    if (prefix.endsWith("/")) {
      return prefix.substring(0, prefix.length() - 1);
    }

    return prefix;
  }
}