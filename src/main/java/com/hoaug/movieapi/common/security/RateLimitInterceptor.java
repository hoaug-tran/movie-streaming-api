package com.hoaug.movieapi.common.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
  private final RateLimiter rateLimiter;

  public RateLimitInterceptor(RateLimiter rateLimiter) {
    this.rateLimiter = rateLimiter;
  }

  @Override
  public boolean preHandle (HttpServletRequest request, HttpServletResponse response,
      Object handler) throws Exception {
    String clientIp = getClientIp(request);
    String path = request.getRequestURI();
    String method = request.getMethod();

    if (path.contains("/auth/oauth/")) {
      return true;
    }

    String key = method + ":" + path + ":" + clientIp;

    int limit = getLimit(path, method);

    if (!rateLimiter.isAllowed(key, limit)) {
      throw new AppException(ErrorCode.RATE_LIMIT_EXCEEDED);
    }

    return true;
  }

  private int getLimit (String path, String method) {
    if (path.contains("/auth/login")) {
      return 5;
    }
    if (path.contains("reviews") || path.contains("comments")) {
      return 50;
    }
    return 100;
  }

  private String getClientIp (HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0];
    }
    return request.getRemoteAddr();
  }
}
