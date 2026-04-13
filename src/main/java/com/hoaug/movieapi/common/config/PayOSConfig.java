package com.hoaug.movieapi.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "payos")
@Data
public class PayOSConfig {

  private String clientId;
  private String apiKey;
  private String checksumKey;
  private String returnUrl;
  private String cancelUrl;
  private String webhookUrl;
}
