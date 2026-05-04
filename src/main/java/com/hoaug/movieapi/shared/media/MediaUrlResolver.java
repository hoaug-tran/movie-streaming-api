package com.hoaug.movieapi.shared.media;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.hoaug.movieapi.config.properties.AppUrlProperties;

@Component
public class MediaUrlResolver {
  private final AppUrlProperties appUrlProperties;

  public MediaUrlResolver (AppUrlProperties appUrlProperties) {
    this.appUrlProperties = appUrlProperties;
  }

  public String resolve (String url) {
    if (!StringUtils.hasText(url)) {
      return url;
    }

    String mediaBase = trimTrailingSlash(appUrlProperties.getMediaBase());
    if (!StringUtils.hasText(mediaBase)) {
      return url;
    }

    if (url.startsWith("/")) {
      return mediaBase + url;
    }

    try {
      URI uri = new URI(url);
      String host = uri.getHost();
      if (host == null || !("localhost".equalsIgnoreCase(host) || "127.0.0.1".equals(host) || "0.0.0.0".equals(host))) {
        return url;
      }

      String path = uri.getRawPath();
      if (!StringUtils.hasText(path)) {
        return url;
      }

      String query = uri.getRawQuery() == null ? "" : "?" + uri.getRawQuery();
      String fragment = uri.getRawFragment() == null ? "" : "#" + uri.getRawFragment();
      return mediaBase + path + query + fragment;
    } catch (URISyntaxException ex) {
      return url;
    }
  }

  private String trimTrailingSlash (String value) {
    if (value == null) {
      return "";
    }
    return value.replaceAll("/+$", "");
  }
}
