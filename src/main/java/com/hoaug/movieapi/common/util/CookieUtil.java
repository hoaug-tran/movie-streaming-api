package com.hoaug.movieapi.common.util;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import com.hoaug.movieapi.modules.auth.application.dto.response.AuthResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
  private static final Duration ACCESS_TOKEN_MAX_AGE = Duration.ofHours(1);
  private static final Duration REFRESH_TOKEN_MAX_AGE = Duration.ofDays(30);

  private CookieUtil () {
  }

  public static void setAuthCookies (HttpServletResponse response, AuthResponse authResponse) {
    addCookie(response, buildCookie("accessToken", authResponse.getAccessToken(), ACCESS_TOKEN_MAX_AGE));
    addCookie(response, buildCookie("refreshToken", authResponse.getRefreshToken(), REFRESH_TOKEN_MAX_AGE));
  }

  public static void clearAuthCookies (HttpServletResponse response) {
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

  private static ResponseCookie buildCookie (String name, String value, Duration maxAge) {
    return ResponseCookie.from(name, value == null ? "" : value)
        .httpOnly(true)
        .secure(false)
        .sameSite("Lax")
        .path("/")
        .maxAge(maxAge)
        .build();
  }
}
