package com.hoaug.movieapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hoaug.movieapi.common.security.RateLimitInterceptor;

@Configuration
public class RateLimitConfig implements WebMvcConfigurer {
  private final RateLimitInterceptor rateLimitInterceptor;

  public RateLimitConfig(RateLimitInterceptor rateLimitInterceptor) {
    this.rateLimitInterceptor = rateLimitInterceptor;
  }

  @Override
  public void addInterceptors (InterceptorRegistry registry) {
    registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/api/**");
  }
}
