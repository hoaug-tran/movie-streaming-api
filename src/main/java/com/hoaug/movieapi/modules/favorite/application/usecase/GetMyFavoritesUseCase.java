package com.hoaug.movieapi.modules.favorite.application.usecase;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.favorite.application.dto.response.FavoriteResponse;
import com.hoaug.movieapi.modules.favorite.application.mapper.FavoriteMapper;
import com.hoaug.movieapi.modules.favorite.domain.repository.FavoriteRepository;

@Component
public class GetMyFavoritesUseCase {

  private final FavoriteRepository favoriteRepository;
  private final FavoriteMapper favoriteMapper;

  public GetMyFavoritesUseCase(FavoriteRepository favoriteRepository,
      FavoriteMapper favoriteMapper) {
    this.favoriteRepository = favoriteRepository;
    this.favoriteMapper = favoriteMapper;
  }

  @Cacheable(cacheNames = "favorites", key = "'user:' + #userId + ':favorites'")
  public List<FavoriteResponse> execute (Long userId) {
    return favoriteRepository.findByUserIdOrderByAddedAtDesc(userId).stream()
        .map(favoriteMapper::toResponse).toList();
  }
}