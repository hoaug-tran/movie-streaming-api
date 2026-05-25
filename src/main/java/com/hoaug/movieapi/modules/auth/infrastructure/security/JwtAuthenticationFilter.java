package com.hoaug.movieapi.modules.auth.infrastructure.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hoaug.movieapi.modules.auth.domain.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final TokenService tokenService;
  private final CustomUserDetailsService customUserDetailsService;

  public JwtAuthenticationFilter(TokenService tokenService,
      CustomUserDetailsService customUserDetailsService) {
    this.tokenService = tokenService;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Override
  protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = null;

    if (request.getCookies() != null) {
      for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
        if ("accessToken".equals(cookie.getName())) {
          token = cookie.getValue();
          break;
        }
      }
    }

    if (!StringUtils.hasText(token)) {
      String authHeader = request.getHeader("Authorization");
      if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7);
      }
    }

    if (!StringUtils.hasText(token)) {
      if (request.getRequestURI().contains("/stream/keys/")) {
        logger.warn("No JWT token found in request for URI: {}", request.getRequestURI());
      }
      filterChain.doFilter(request, response);
      return;
    }

    try {
      if (token != null && token.startsWith("\"") && token.endsWith("\"")) {
        token = token.substring(1, token.length() - 1);
      }
      String username = tokenService.extractUsername(token);

      if (StringUtils.hasText(username)
          && SecurityContextHolder.getContext().getAuthentication() == null) {

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if (tokenService.isValidToken(token, userDetails.getUsername())) {
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());

          authenticationToken
              .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }
    } catch (Exception e) {
      logger.warn("Xác thực JWT thất bại: {}. Token prefix: {}", e.getMessage(),
          (token != null && token.length() > 10) ? token.substring(0, 10) : "short-token");
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
  }
}