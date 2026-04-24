package com.hoaug.movieapi.modules.favorite.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.favorite.application.dto.response.FavoriteListResponse;
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

  public FavoriteListResponse execute (Long userId) {
    List<FavoriteResponse> items = favoriteRepository.findByUserIdOrderByAddedAtDesc(userId).stream()
        .map(favoriteMapper::toResponse).toList();
    return new FavoriteListResponse(items);
  }
}