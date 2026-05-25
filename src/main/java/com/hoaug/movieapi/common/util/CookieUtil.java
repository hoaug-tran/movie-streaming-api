package com.hoaug.movieapi.common.util;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.auth.application.dto.response.AuthResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtil {
  private static final Duration ACCESS_TOKEN_MAX_AGE = Duration.ofHours(1);
  private static final Duration REFRESH_TOKEN_MAX_AGE = Duration.ofDays(30);

  private final boolean secure;
  private final String sameSite;
  private final String domain;

  public CookieUtil(@Value("${app.cookie.secure:false}") boolean secure,
      @Value("${app.cookie.same-site:Lax}") String sameSite,
      @Value("${app.cookie.domain:}") String domain) {
    this.secure = secure;
    this.sameSite = sameSite;
    this.domain = domain == null ? "" : domain.trim();
  }

  public void setAuthCookies (HttpServletResponse response, AuthResponse authResponse) {
    addCookie(response,
        buildCookie("accessToken", authResponse.getAccessToken(), ACCESS_TOKEN_MAX_AGE));
    addCookie(response,
        buildCookie("refreshToken", authResponse.getRefreshToken(), REFRESH_TOKEN_MAX_AGE));
  }

  public void clearAuthCookies (HttpServletResponse response) {
    addCookie(response, buildCookie("accessToken", "", Duration.ZERO));
    addCookie(response, buildCookie("refreshToken", "", Duration.ZERO));
  }

  public static void addCookie (HttpServletResponse response, ResponseCookie cookie) {
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  public static String getCookieValue (HttpServletRequest request, String name) {
    if (request.getCookies() == null) {
      return null;
    }

    for (Cookie cookie : request.getCookies()) {
      if (name.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }

    return null;
  }

  public ResponseCookie buildCookie (String name, String value, Duration maxAge) {
    ResponseCookie.ResponseCookieBuilder builder = ResponseCookie
        .from(name, value == null ? "" : value).httpOnly(false).secure(secure).sameSite(sameSite)
        .path("/").maxAge(maxAge);
    if (!domain.isEmpty()) {
      builder.domain(domain);
    }
    return builder.build();
  }

  public boolean isSecure () {
    return secure;
  }

  public String getSameSite () {
    return sameSite;
  }

  public String getDomain () {
    return domain;
  }
}
