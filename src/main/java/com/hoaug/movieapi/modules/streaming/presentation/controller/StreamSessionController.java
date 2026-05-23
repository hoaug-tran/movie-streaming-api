package com.hoaug.movieapi.modules.streaming.presentation.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.streaming.application.usecase.HeartbeatStreamSessionUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.StartStreamSessionUseCase;
import com.hoaug.movieapi.modules.streaming.application.usecase.StopStreamSessionUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${api.prefix:/api/v1}/stream/sessions")
public class StreamSessionController {

  private final StartStreamSessionUseCase startStreamSessionUseCase;
  private final HeartbeatStreamSessionUseCase heartbeatStreamSessionUseCase;
  private final StopStreamSessionUseCase stopStreamSessionUseCase;
  private final AuthUserRepository authUserRepository;

  public StreamSessionController(StartStreamSessionUseCase startStreamSessionUseCase,
      HeartbeatStreamSessionUseCase heartbeatStreamSessionUseCase,
      StopStreamSessionUseCase stopStreamSessionUseCase, AuthUserRepository authUserRepository) {
    this.startStreamSessionUseCase = startStreamSessionUseCase;
    this.heartbeatStreamSessionUseCase = heartbeatStreamSessionUseCase;
    this.stopStreamSessionUseCase = stopStreamSessionUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PostMapping("/start")
  @ResponseStatus(HttpStatus.CREATED)
  public Map<String, Long> start (@RequestBody Map<String, String> body,
      Authentication authentication, HttpServletRequest request) {
    User user = resolveUser(authentication);
    Long sessionId = startStreamSessionUseCase.execute(user.getId(),
        body.getOrDefault("deviceName", "Unknown"), body.getOrDefault("deviceType", "WEB"),
        request.getHeader("User-Agent"), request.getRemoteAddr());
    return Map.of("sessionId", sessionId);
  }

  @PutMapping("/{sessionId}/heartbeat")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void heartbeat (@PathVariable Long sessionId, Authentication authentication) {
    Long userId = null;
    if (authentication != null && authentication.isAuthenticated()
        && !"anonymousUser".equals(authentication.getPrincipal())) {
      userId = resolveUser(authentication).getId();
    }
    heartbeatStreamSessionUseCase.execute(sessionId, userId);
  }

  @DeleteMapping("/{sessionId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void stop (@PathVariable Long sessionId, Authentication authentication) {
    stopStreamSessionUseCase.execute(sessionId, resolveUser(authentication).getId());
  }

  private User resolveUser (Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getPrincipal())) {
      throw new AppException(ErrorCode.UNAUTHORIZED);
    }
    return authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
  }
}
