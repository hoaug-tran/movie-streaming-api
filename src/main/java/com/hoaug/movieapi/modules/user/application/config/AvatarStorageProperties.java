package com.hoaug.movieapi.modules.user.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.storage.avatar")
public class AvatarStorageProperties {
  private String directory;
  private String publicBaseUrl;
  private long maxBytes = 5242880L;

  public String getDirectory () {
    return directory;
  }

  public void setDirectory (String directory) {
    this.directory = directory;
  }

  public String getPublicBaseUrl () {
    return publicBaseUrl;
  }

  public void setPublicBaseUrl (String publicBaseUrl) {
    this.publicBaseUrl = publicBaseUrl;
  }

  public long getMaxBytes () {
    return maxBytes;
  }

  public void setMaxBytes (long maxBytes) {
    this.maxBytes = maxBytes;
  }
}
