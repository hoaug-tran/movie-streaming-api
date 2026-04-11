package com.hoaug.movieapi.modules.email.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app.url")
public class AppUrlConfig {
  private String base;
  private String resetPassword;
  private String emailVerification;
}
