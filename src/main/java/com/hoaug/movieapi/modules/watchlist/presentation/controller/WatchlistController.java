package com.hoaug.movieapi.modules.watchlist.presentation.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.user.domain.model.User;
import com.hoaug.movieapi.modules.watchlist.application.dto.response.MovieInWatchlistResponse;
import com.hoaug.movieapi.modules.watchlist.application.dto.response.WatchlistResponse;
import com.hoaug.movieapi.modules.watchlist.application.usecase.AddWatchlistUseCase;
import com.hoaug.movieapi.modules.watchlist.application.usecase.CheckMovieInWatchlistUseCase;
import com.hoaug.movieapi.modules.watchlist.application.usecase.GetMyWatchlistUseCase;
import com.hoaug.movieapi.modules.watchlist.application.usecase.RemoveWatchlistUseCase;

@RestController
@RequestMapping("${api.prefix:/api/v1}/watchlists")
public class WatchlistController {

  private final AddWatchlistUseCase addWatchlistUseCase;
  private final RemoveWatchlistUseCase removeWatchlistUseCase;
  private final GetMyWatchlistUseCase getMyWatchlistUseCase;
  private final CheckMovieInWatchlistUseCase checkMovieInWatchlistUseCase;
  private final AuthUserRepository authUserRepository;

  public WatchlistController(AddWatchlistUseCase addWatchlistUseCase,
      RemoveWatchlistUseCase removeWatchlistUseCase, GetMyWatchlistUseCase getMyWatchlistUseCase,
      CheckMovieInWatchlistUseCase checkMovieInWatchlistUseCase,
      AuthUserRepository authUserRepository) {
    this.addWatchlistUseCase = addWatchlistUseCase;
    this.removeWatchlistUseCase = removeWatchlistUseCase;
    this.getMyWatchlistUseCase = getMyWatchlistUseCase;
    this.checkMovieInWatchlistUseCase = checkMovieInWatchlistUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PostMapping("/{movieId}")
  public WatchlistResponse add (Authentication authentication, @PathVariable Long movieId) {
    return addWatchlistUseCase.execute(getCurrentUserId(authentication), movieId);
  }

  @DeleteMapping("/{movieId}")
  public void remove (Authentication authentication, @PathVariable Long movieId) {
    removeWatchlistUseCase.execute(getCurrentUserId(authentication), movieId);
  }

  @GetMapping("/me")
  public List<WatchlistResponse> getMyWatchlist (Authentication authentication) {
    return getMyWatchlistUseCase.execute(getCurrentUserId(authentication));
  }

  @GetMapping("/me/check/{movieId}")
  public MovieInWatchlistResponse check (Authentication authentication,
      @PathVariable Long movieId) {
    return checkMovieInWatchlistUseCase.execute(getCurrentUserId(authentication), movieId);
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}