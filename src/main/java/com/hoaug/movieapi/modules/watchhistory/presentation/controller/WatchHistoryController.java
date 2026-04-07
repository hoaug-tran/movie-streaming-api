package com.hoaug.movieapi.modules.watchhistory.presentation.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
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
  public WatchHistoryResponse upsert (Authentication authentication,
      @Valid @RequestBody UpsertWatchHistoryRequest request) {
    return upsertWatchHistoryUseCase.execute(getCurrentUserId(authentication), request);
  }

  @GetMapping("/me")
  public List<WatchHistoryResponse> getMyWatchHistories (Authentication authentication) {
    return getMyWatchHistoriesUseCase.execute(getCurrentUserId(authentication));
  }

  @GetMapping("/me/continue-watching")
  public List<ContinueWatchingResponse> getContinueWatching (Authentication authentication) {
    return getContinueWatchingUseCase.execute(getCurrentUserId(authentication));
  }

  @GetMapping("/me/movie/{movieId}")
  public List<WatchHistoryResponse> getMyMovieWatchHistory (Authentication authentication,
      @PathVariable Long movieId) {
    return getMyMovieWatchHistoryUseCase.execute(getCurrentUserId(authentication), movieId);
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}