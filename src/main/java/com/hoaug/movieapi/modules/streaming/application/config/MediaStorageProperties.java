package com.hoaug.movieapi.modules.streaming.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.storage.media")
public class MediaStorageProperties {
  private String publicBaseUrl = "http://localhost";
  private String moviesDataDirectory = "F:/movie-storage/data/movies";
  private String seriesDataDirectory = "F:/movie-storage/data/series";
  private String adsDataDirectory = "F:/movie-storage/data/ads";
  private String othersDataDirectory = "F:/movie-storage/data/others";
  private String imagesDirectory = "F:/movie-storage/images";
  private String hlsDirectory = "F:/movie-storage/hls";
  private String keysDirectory = "F:/movie-storage/keys";
  private long maxUploadBytes = 5368709120L;
  private String ffmpegPath = "ffmpeg";

  public String getPublicBaseUrl () {
    return publicBaseUrl;
  }

  public void setPublicBaseUrl (String publicBaseUrl) {
    this.publicBaseUrl = publicBaseUrl;
  }

  public String getMoviesDataDirectory () {
    return moviesDataDirectory;
  }

  public void setMoviesDataDirectory (String moviesDataDirectory) {
    this.moviesDataDirectory = moviesDataDirectory;
  }

  public String getSeriesDataDirectory () {
    return seriesDataDirectory;
  }

  public void setSeriesDataDirectory (String seriesDataDirectory) {
    this.seriesDataDirectory = seriesDataDirectory;
  }

  public String getAdsDataDirectory () {
    return adsDataDirectory;
  }

  public void setAdsDataDirectory (String adsDataDirectory) {
    this.adsDataDirectory = adsDataDirectory;
  }

  public String getOthersDataDirectory () {
    return othersDataDirectory;
  }

  public void setOthersDataDirectory (String othersDataDirectory) {
    this.othersDataDirectory = othersDataDirectory;
  }

  public String getImagesDirectory () {
    return imagesDirectory;
  }

  public void setImagesDirectory (String imagesDirectory) {
    this.imagesDirectory = imagesDirectory;
  }

  public String getHlsDirectory () {
    return hlsDirectory;
  }

  public void setHlsDirectory (String hlsDirectory) {
    this.hlsDirectory = hlsDirectory;
  }

  public String getKeysDirectory () {
    return keysDirectory;
  }

  public void setKeysDirectory (String keysDirectory) {
    this.keysDirectory = keysDirectory;
  }

  public long getMaxUploadBytes () {
    return maxUploadBytes;
  }

  public void setMaxUploadBytes (long maxUploadBytes) {
    this.maxUploadBytes = maxUploadBytes;
  }

  public String getFfmpegPath () {
    return ffmpegPath;
  }

  public void setFfmpegPath (String ffmpegPath) {
    this.ffmpegPath = ffmpegPath;
  }
}
