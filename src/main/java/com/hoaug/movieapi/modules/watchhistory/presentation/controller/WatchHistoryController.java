package com.hoaug.movieapi.modules.watchhistory.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.watchhistory.application.dto.request.UpsertWatchHistoryRequest;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.ContinueWatchingResponse;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.WatchHistoryResponse;
import com.hoaug.movieapi.modules.watchhistory.application.usecase.GetContinueWatchingUseCase;
import com.hoaug.movieapi.modules.watchhistory.application.usecase.GetMyMovieWatchHistoryUseCase;
import com.hoaug.movieapi.modules.watchhistory.application.usecase.GetMyWatchHistoriesUseCase;
import com.hoaug.movieapi.modules.watchhistory.application.usecase.UpsertWatchHistoryUseCase;

import jakarta.validation.Valid;

/**
 * HTTP Status Codes: - 200 OK: GET, PUT successful - 201 Created: POST creates new record - 204 No
 * Content: DELETE successful - 400 Bad Request: Invalid input - 401 Unauthorized: Not authenticated
 * - 404 Not Found: Resource not found - 500 Internal Error: Server error
 */
@RestController
@RequestMapping("${api.prefix:/api/v1}/watch-histories")
public class WatchHistoryController {

  private final UpsertWatchHistoryUseCase upsertWatchHistoryUseCase;
  private final GetMyWatchHistoriesUseCase getMyWatchHistoriesUseCase;
  private final GetContinueWatchingUseCase getContinueWatchingUseCase;
  private final GetMyMovieWatchHistoryUseCase getMyMovieWatchHistoryUseCase;
  private final AuthUserRepository authUserRepository;

  public WatchHistoryController(UpsertWatchHistoryUseCase upsertWatchHistoryUseCase,
      GetMyWatchHistoriesUseCase getMyWatchHistoriesUseCase,
      GetContinueWatchingUseCase getContinueWatchingUseCase,
      GetMyMovieWatchHistoryUseCase getMyMovieWatchHistoryUseCase,
      AuthUserRepository authUserRepository) {
    this.upsertWatchHistoryUseCase = upsertWatchHistoryUseCase;
    this.getMyWatchHistoriesUseCase = getMyWatchHistoriesUseCase;
    this.getContinueWatchingUseCase = getContinueWatchingUseCase;
    this.getMyMovieWatchHistoryUseCase = getMyMovieWatchHistoryUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PostMapping
  public ResponseEntity<WatchHistoryResponse> upsert (Authentication authentication,
      @Valid @RequestBody UpsertWatchHistoryRequest request) {
    Long userId = getCurrentUserId(authentication);
    WatchHistoryResponse response = upsertWatchHistoryUseCase.execute(userId, request);
    return ResponseUtil.created(response);
  }

  @GetMapping("/me")
  public ResponseEntity<List<WatchHistoryResponse>> getMyWatchHistories (
      Authentication authentication) {
    Long userId = getCurrentUserId(authentication);
    return ResponseUtil.ok(getMyWatchHistoriesUseCase.execute(userId).getItems());
  }

  @GetMapping("/me/continue-watching")
  public ResponseEntity<List<ContinueWatchingResponse>> getContinueWatching (
      Authentication authentication) {
    Long userId = getCurrentUserId(authentication);
    List<ContinueWatchingResponse> items = getContinueWatchingUseCase.execute(userId);
    return ResponseUtil.ok(items);
  }

  @GetMapping("/me/movie/{movieId}")
  public ResponseEntity<List<WatchHistoryResponse>> getMyMovieWatchHistory (
      Authentication authentication, @PathVariable Long movieId) {
    Long userId = getCurrentUserId(authentication);
    List<WatchHistoryResponse> histories = getMyMovieWatchHistoryUseCase.execute(userId, movieId);
    return ResponseUtil.ok(histories);
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}