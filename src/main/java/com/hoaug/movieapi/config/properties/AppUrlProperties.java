package com.hoaug.movieapi.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.url")
public class AppUrlProperties {
  private String mediaBase = "http://localhost";

  public String getMediaBase () {
    return mediaBase;
  }

  public void setMediaBase (String mediaBase) {
    this.mediaBase = mediaBase;
  }
}
