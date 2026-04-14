package com.hoaug.movieapi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.payos.PayOS;

@Configuration
public class PayOSBeanConfig {

  @Bean
  public PayOS payOS (PayOSConfig payOSConfig) {
    return new PayOS(payOSConfig.getClientId(), payOSConfig.getApiKey(),
        payOSConfig.getChecksumKey());
  }

  @Bean
  public ObjectMapper objectMapper () {
    return new ObjectMapper();
  }
}
