package com.hoaug.movieapi.modules.favorite.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.favorite.domain.repository.FavoriteRepository;

@Component
public class RemoveFavoriteUseCase {

  private final FavoriteRepository favoriteRepository;

  public RemoveFavoriteUseCase(FavoriteRepository favoriteRepository) {
    this.favoriteRepository = favoriteRepository;
  }

  public void execute (Long userId, Long movieId) {
    favoriteRepository.findByUserIdAndMovieId(userId, movieId)
        .ifPresent(favoriteRepository::delete);
  }
}