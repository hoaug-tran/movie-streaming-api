package com.hoaug.movieapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hoaug.movieapi.common.security.ActivityLogInterceptor;
import com.hoaug.movieapi.common.security.RateLimitInterceptor;

@Configuration
public class RateLimitConfig implements WebMvcConfigurer {
  private final RateLimitInterceptor rateLimitInterceptor;
  private final ActivityLogInterceptor activityLogInterceptor;

  public RateLimitConfig(RateLimitInterceptor rateLimitInterceptor, ActivityLogInterceptor activityLogInterceptor) {
    this.rateLimitInterceptor = rateLimitInterceptor;
    this.activityLogInterceptor = activityLogInterceptor;
  }

  @Override
  public void addInterceptors (InterceptorRegistry registry) {
    registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/api/v1/**");
    registry.addInterceptor(activityLogInterceptor).addPathPatterns("/api/v1/**");
  }
}

