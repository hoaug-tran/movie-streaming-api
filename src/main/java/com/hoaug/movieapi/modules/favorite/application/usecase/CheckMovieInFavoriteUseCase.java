package com.hoaug.movieapi.modules.favorite.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.favorite.application.dto.response.MovieInFavoriteResponse;
import com.hoaug.movieapi.modules.favorite.domain.repository.FavoriteRepository;

@Component
public class CheckMovieInFavoriteUseCase {

  private final FavoriteRepository favoriteRepository;

  public CheckMovieInFavoriteUseCase(FavoriteRepository favoriteRepository) {
    this.favoriteRepository = favoriteRepository;
  }

  public MovieInFavoriteResponse execute (Long userId, Long movieId) {
    MovieInFavoriteResponse response = new MovieInFavoriteResponse();
    response.setMovieId(movieId);
    response.setInFavorite(favoriteRepository.findByUserIdAndMovieId(userId, movieId).isPresent());
    return response;
  }
}