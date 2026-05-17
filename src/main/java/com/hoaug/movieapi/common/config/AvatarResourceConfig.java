package com.hoaug.movieapi.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hoaug.movieapi.modules.user.application.config.AvatarStorageProperties;

@Configuration
public class AvatarResourceConfig implements WebMvcConfigurer {

  private final AvatarStorageProperties avatarStorageProperties;

  public AvatarResourceConfig(AvatarStorageProperties avatarStorageProperties) {
    this.avatarStorageProperties = avatarStorageProperties;
  }

  @Override
  public void addResourceHandlers (ResourceHandlerRegistry registry) {
    String directory = avatarStorageProperties.getDirectory();
    if (directory == null || directory.isBlank()) {
      return;
    }

    String location = directory.replace("\\", "/");
    if (!location.startsWith("/")) {
      location = "/" + location;
    }
    if (!location.endsWith("/")) {
      location = location + "/";
    }

    registry.addResourceHandler("/avatar/**").addResourceLocations("file:" + location)
        .setCachePeriod(3600);
  }
}
