package com.hoaug.movieapi.modules.favorite.application.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.modules.favorite.domain.repository.FavoriteRepository;

@Component
public class ClearMyFavoritesUseCase {

  private final FavoriteRepository favoriteRepository;

  public ClearMyFavoritesUseCase(FavoriteRepository favoriteRepository) {
    this.favoriteRepository = favoriteRepository;
  }

  @Transactional
  public void execute (Long userId) {
    favoriteRepository.deleteByUserId(userId);
  }
}
