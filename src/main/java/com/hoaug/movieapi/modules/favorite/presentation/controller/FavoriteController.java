package com.hoaug.movieapi.modules.favorite.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.favorite.application.dto.response.FavoriteResponse;
import com.hoaug.movieapi.modules.favorite.application.dto.response.MovieInFavoriteResponse;
import com.hoaug.movieapi.modules.favorite.application.usecase.AddFavoriteUseCase;
import com.hoaug.movieapi.modules.favorite.application.usecase.CheckMovieInFavoriteUseCase;
import com.hoaug.movieapi.modules.favorite.application.usecase.GetMyFavoritesUseCase;
import com.hoaug.movieapi.modules.favorite.application.usecase.RemoveFavoriteUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

@RestController
@RequestMapping("${api.prefix:/api/v1}/favorites")
public class FavoriteController {

  private final AddFavoriteUseCase addFavoriteUseCase;
  private final RemoveFavoriteUseCase removeFavoriteUseCase;
  private final GetMyFavoritesUseCase getMyFavoritesUseCase;
  private final CheckMovieInFavoriteUseCase checkMovieInFavoriteUseCase;
  private final AuthUserRepository authUserRepository;

  public FavoriteController(AddFavoriteUseCase addFavoriteUseCase,
      RemoveFavoriteUseCase removeFavoriteUseCase, GetMyFavoritesUseCase getMyFavoritesUseCase,
      CheckMovieInFavoriteUseCase checkMovieInFavoriteUseCase,
      AuthUserRepository authUserRepository) {
    this.addFavoriteUseCase = addFavoriteUseCase;
    this.removeFavoriteUseCase = removeFavoriteUseCase;
    this.getMyFavoritesUseCase = getMyFavoritesUseCase;
    this.checkMovieInFavoriteUseCase = checkMovieInFavoriteUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PostMapping("/{movieId}")
  public ResponseEntity<FavoriteResponse> add (Authentication authentication,
      @PathVariable Long movieId) {
    FavoriteResponse response = addFavoriteUseCase.execute(getCurrentUserId(authentication),
        movieId);
    return ResponseUtil.created(response);
  }

  @DeleteMapping("/{movieId}")
  public ResponseEntity<Void> remove (Authentication authentication, @PathVariable Long movieId) {
    removeFavoriteUseCase.execute(getCurrentUserId(authentication), movieId);
    return ResponseUtil.noContent();
  }

  @GetMapping("/me")
  public ResponseEntity<List<FavoriteResponse>> getMyFavorites (Authentication authentication) {
    return ResponseUtil.ok(getMyFavoritesUseCase.execute(getCurrentUserId(authentication)).getItems());
  }

  @GetMapping("/me/check/{movieId}")
  public ResponseEntity<MovieInFavoriteResponse> check (Authentication authentication,
      @PathVariable Long movieId) {
    MovieInFavoriteResponse response = checkMovieInFavoriteUseCase
        .execute(getCurrentUserId(authentication), movieId);
    return ResponseUtil.ok(response);
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}