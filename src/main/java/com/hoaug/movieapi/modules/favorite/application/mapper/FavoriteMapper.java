package com.hoaug.movieapi.modules.favorite.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.favorite.application.dto.response.FavoriteResponse;
import com.hoaug.movieapi.modules.favorite.domain.model.Favorite;

@Component
public class FavoriteMapper {

  public FavoriteResponse toResponse (Favorite favorite) {
    FavoriteResponse response = new FavoriteResponse();
    response.setId(favorite.getId());
    response.setMovieId(favorite.getMovieId());
    response.setAddedAt(favorite.getAddedAt());
    return response;
  }
}