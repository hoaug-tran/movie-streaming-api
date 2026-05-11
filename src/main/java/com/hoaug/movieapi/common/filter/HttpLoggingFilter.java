package com.hoaug.movieapi.common.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HttpLoggingFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(HttpLoggingFilter.class);

  public static volatile long lastResponseTime = 0;
  public static volatile long totalRequests = 0;
  public static volatile long activeRequests = 0;

  @Override
  protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    long startTime = System.currentTimeMillis();
    activeRequests++;
    totalRequests++;

    try {
      logger.info("→ {} {} | IP: {}", request.getMethod(), request.getRequestURI(),
          getClientIp(request));

      filterChain.doFilter(request, response);

      long duration = System.currentTimeMillis() - startTime;
      lastResponseTime = duration;
      if (response.getStatus() >= 400) {
        logger.warn("← {} {} | Status: {} | Time: {}ms", request.getMethod(),
            request.getRequestURI(), response.getStatus(), duration);
      } else {
        logger.info("← {} {} | Status: {} | Time: {}ms", request.getMethod(),
            request.getRequestURI(), response.getStatus(), duration);
      }
    } catch (Exception e) {
      long duration = System.currentTimeMillis() - startTime;
      lastResponseTime = duration;
      logger.error("✗ {} {} | TIME: {}ms | ERROR: {}", request.getMethod(), request.getRequestURI(),
          duration, e.getMessage());
      throw e;
    } finally {
      activeRequests--;
    }
  }

  private String getClientIp (HttpServletRequest request) {
    String[] headers = { "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP" };

    for (String header : headers) {
      String ip = request.getHeader(header);
      if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
        return normalizeIp(ip.split(",")[0].trim());
      }
    }

    return normalizeIp(request.getRemoteAddr());
  }

  private String normalizeIp (String ip) {
    if (ip == null)
      return null;

    if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("::1")) {
      return "127.0.0.1";
    }

    if (ip.startsWith("::ffff:")) {
      return ip.substring(7);
    }

    return ip;
  }
}
