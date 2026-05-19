package com.hoaug.movieapi.modules.streaming.application.service;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.streaming.application.config.MediaStorageProperties;

@Component
public class StreamUrlService {
  private final MediaStorageProperties properties;

  public StreamUrlService(MediaStorageProperties properties) {
    this.properties = properties;
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

  public String episodeSegmentUrl (Long episodeId, String quality, String segmentFilename) {
    return joinBaseUrl("/stream/series/episodes/" + episodeId + "/" + quality + "/" + segmentFilename);
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
