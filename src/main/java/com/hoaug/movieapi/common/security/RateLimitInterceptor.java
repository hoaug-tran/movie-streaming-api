package com.hoaug.movieapi.common.security;

import java.util.Locale;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
  private static final long ONE_MINUTE = 60_000L;
  private static final long TEN_MINUTES = 10 * ONE_MINUTE;
  private static final long ONE_HOUR = 60 * ONE_MINUTE;

  private final RateLimiter rateLimiter;

  public RateLimitInterceptor(RateLimiter rateLimiter) {
    this.rateLimiter = rateLimiter;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) throws Exception {
    String path = request.getRequestURI();
    String method = request.getMethod().toUpperCase(Locale.ROOT);

    if (shouldSkip(path, method)) {
      return true;
    }

    RateLimitPolicy policy = policyFor(path, method);
    String key = policy.keyPrefix + ":" + method + ":" + path + ":" + resolveActor(request, policy.perUser);

    if (!rateLimiter.isAllowed(key, policy.limit, policy.windowMillis)) {
      throw new AppException(ErrorCode.RATE_LIMIT_EXCEEDED);
    }

    return true;
  }

  private boolean shouldSkip(String path, String method) {
    return path.contains("/auth/oauth/")
        || "OPTIONS".equals(method)
        || path.startsWith("/api/v1/stream/keys/");
  }

  private RateLimitPolicy policyFor(String path, String method) {
    if (path.contains("/auth/login")) {
      return new RateLimitPolicy("auth-login", 5, TEN_MINUTES, false);
    }
    if (path.contains("/auth/register") || path.contains("/auth/forgot-password")
        || path.contains("/auth/reset-password") || path.contains("/auth/otp")) {
      return new RateLimitPolicy("auth-sensitive", 6, TEN_MINUTES, false);
    }
    if (path.contains("/chat/stream")) {
      return new RateLimitPolicy("chat-stream", 20, ONE_HOUR, true);
    }
    if (path.contains("/stream/offline") || path.contains("/stream/sessions")) {
      return new RateLimitPolicy("stream-protected", 30, ONE_MINUTE, true);
    }
    if (path.contains("/support/contact")) {
      return new RateLimitPolicy("support-contact", 3, TEN_MINUTES, false);
    }
    if (path.contains("/advertisements/views")) {
      return new RateLimitPolicy("ad-view", 60, ONE_MINUTE, false);
    }
    if (!"GET".equals(method)) {
      return new RateLimitPolicy("write", 60, ONE_MINUTE, true);
    }
    if (path.contains("reviews") || path.contains("comments")) {
      return new RateLimitPolicy("discussion-read", 120, ONE_MINUTE, false);
    }
    if (path.contains("/movies/search")) {
      return new RateLimitPolicy("search", 80, ONE_MINUTE, false);
    }
    return new RateLimitPolicy("api-read", 240, ONE_MINUTE, false);
  }

  private String resolveActor(HttpServletRequest request, boolean preferUser) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (preferUser && authentication != null && authentication.isAuthenticated()
        && authentication.getName() != null && !"anonymousUser".equals(authentication.getName())) {
      return "user:" + authentication.getName();
    }
    return "ip:" + getClientIp(request);
  }

  private String getClientIp(HttpServletRequest request) {
    String cfConnectingIp = request.getHeader("CF-Connecting-IP");
    if (cfConnectingIp != null && !cfConnectingIp.isBlank()) {
      return cfConnectingIp.trim();
    }

    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isBlank()) {
      return xForwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }

  private record RateLimitPolicy(String keyPrefix, int limit, long windowMillis, boolean perUser) {}
}
